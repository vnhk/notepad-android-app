package com.example.myapplication.reponotes;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Note;
import com.example.myapplication.OfflineActivity;
import com.example.myapplication.R;
import com.example.myapplication.db.DBController;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteSynchronizer {
    private static final String BASIC_URL = "https://noter-word-app.herokuapp.com";
    private static final String URL_QUICK_NOTES = BASIC_URL + "/quickNotes/get?sorting=mDesc&startIndex=0&endIndex=10000000";
    private static final String URL_DOCUMENTS = BASIC_URL + "/documents/search?sorting=mDesc";
    private static final String URL_CALENDAR_NOTES = BASIC_URL + "/calendars/notes/search?sorting=mDesc";
    private static final String URL_CALENDAR_NOTES_DATES_PATTERN = BASIC_URL + "/calendars/notes/%d/dates";
    private static DBController dbController;


    public static void synchronizeNotes(String token, Context applicationContext) {
        OfflineActivity.noSynchronization = false;
        dbController = new DBController(applicationContext);
        Thread saveNotes = new Thread(() -> {
            SQLiteDatabase.create(SQLiteCursor::new);
            List<Note> notes = null;
            try {
                notes = receiveNotes(token);
                updateDatabase(notes, applicationContext);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        saveNotes.start();
    }

    private static void updateDatabase(List<Note> notes, Context applicationContext) {
        List<Note> databaseNotes = getNotes(applicationContext);

        if (notes.size() == 0) {
            return;
        }

        for (Note databaseNote : databaseNotes) {
            int databaseNoteId = databaseNote.getId();
            Optional<Note> any = notes.stream().filter(e -> e.getId() == databaseNoteId).findAny();

            if (any.isPresent()) {
                Note note = any.get();

                if (note.getModifyDate().after(databaseNote.getModifyDate())) {
                    dbController.deleteNote(databaseNote);
                    saveNote(note);
                }

                notes.remove(note);
            } else {
                dbController.deleteNote(databaseNote);
            }
        }

        for (Note note : notes) {
            saveNote(note);
        }
    }

    private static List<Note> receiveNotes(String token) throws InterruptedException {
        List<Note> allNotes = new ArrayList<>();
        Thread calendarNotesReceiver = new Thread(() -> {
            try {
                allNotes.addAll(receiveCalendarNotes(token));
                allNotes.addAll(receiveQuickNotes(token));
                allNotes.addAll(receiveDocuments(token));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        calendarNotesReceiver.start();

        calendarNotesReceiver.join();

        OfflineActivity.noSynchronization = true;

        return allNotes;
    }


    private static List<Note> receiveCalendarNotes(String token) throws JSONException {
        List<Note> result = new ArrayList<>();

        JSONObject response = getResponse(URL_CALENDAR_NOTES, token);
        if (response != null) {
            JSONArray notes = response.getJSONArray("notes");
            for (int i = 0; i < notes.length(); i++) {
                JSONObject noteJson = notes.getJSONObject(i);

                Note note = createNoteWithAttr(noteJson);

                String title = noteJson.getString("title");
                String time = noteJson.getString("time");
                String calendar = (noteJson.getJSONObject("calendar")).getString("name");

                note.setType(Note.CALENDAR_NOTE);
                note.setImageResource(R.drawable.icon_calendar_note);
                note.setHeader(title + " at " + time);

                JSONObject datesResponse = getResponse(String.format(URL_CALENDAR_NOTES_DATES_PATTERN, note.getId()), token);
                if (datesResponse != null) {
                    JSONArray datesArray = datesResponse.getJSONArray("dates");
                    StringBuilder datesBuilder = new StringBuilder();
                    for (int dateI = 0; dateI < datesArray.length(); dateI++) {
                        String date = datesArray.getString(dateI);
                        datesBuilder.append(date.split("#")[1]);
                        datesBuilder.append(", ");
                    }
                    note.setSecondary(calendar + ": " + datesBuilder.toString());
                }
                result.add(note);
            }
        }
        return result;
    }


    private static Note createNoteWithAttr(JSONObject noteJson) throws JSONException {
        Note note = new Note();
        int id = noteJson.getInt("id");
        String content = noteJson.getString("content");
        String modifyDate = noteJson.getString("modifyDate");
        String createDate = noteJson.getString("createDate");
        JSONArray tagsArray = noteJson.getJSONArray("tags");
        StringBuilder builder = new StringBuilder();
        String tags = getTags(tagsArray, builder);

        note.setTags(tags);
        note.setPersistInfoDisplay("Create date: " + createDate + "\nModify date: " + modifyDate);
        note.setId(id);

        Timestamp timestamp = getTimestamp(modifyDate);

        note.setModifyDate(timestamp);
        note.setContent(content);

        return note;
    }

    private static List<Note> receiveQuickNotes(String token) throws JSONException {
        List<Note> result = new ArrayList<>();

        JSONObject response = getResponse(URL_QUICK_NOTES, token);
        if (response != null) {
            JSONArray notes = response.getJSONArray("notes");
            for (int i = 0; i < notes.length(); i++) {

                JSONObject noteJson = notes.getJSONObject(i);

                Note note = createNoteWithAttr(noteJson);
                String plainTextContent = noteJson.getString("plainTextContent");

                note.setSecondary(plainTextContent);
                note.setType(Note.QUICK_NOTE);
                note.setImageResource(R.drawable.icon_quick_note);

                result.add(note);
            }
        }
        return result;
    }

    private static List<Note> receiveDocuments(String token) throws JSONException {
        List<Note> result = new ArrayList<>();

        JSONObject response = getResponse(URL_DOCUMENTS, token);
        if (response != null) {
            JSONArray notes = response.getJSONArray("notes");
            for (int i = 0; i < notes.length(); i++) {

                JSONObject noteJson = notes.getJSONObject(i);

                Note note = createNoteWithAttr(noteJson);

                String name = noteJson.getString("name");
                String description = noteJson.getString("description");

                note.setHeader(name);
                note.setSecondary(description);

                note.setType(Note.DOCUMENT);
                note.setImageResource(R.drawable.icon_document);

                result.add(note);
            }
        }
        return result;
    }

    private static Timestamp getTimestamp(String modifyDate) {
        return Timestamp
                .valueOf(modifyDate.split("T")[0] + " " + modifyDate.split("T")[1]);
    }

    @NotNull
    private static String getTags(JSONArray tagsArray, StringBuilder builder) throws JSONException {
        for (int tagI = 0; tagI < tagsArray.length(); tagI++) {
            builder.append(tagsArray.get(tagI));
            builder.append(", ");
        }

        return builder.toString();
    }


    private static JSONObject getResponse(String URL, String token) {
        try {
            URL get = new URL(URL);
            HttpURLConnection con;
            con = (HttpURLConnection) get.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", token);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            con.getInputStream()));
            String inputLine;

            StringBuilder builder = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine);
            in.close();

            return new JSONObject(builder.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveNote(Note n) {
        try {
            dbController.addNote(n);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static List<Note> getNotes(Context context) {
        if (dbController == null) {
            dbController = new DBController(context);
        }
        return dbController.getNotes();
    }
}
