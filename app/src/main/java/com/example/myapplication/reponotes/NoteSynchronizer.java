package com.example.myapplication.reponotes;

import com.example.myapplication.Note;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoteSynchronizer {
    private static final String BASIC_URL = "https://noter-word-app.herokuapp.com";
    private static final String URL_QUICK_NOTES = BASIC_URL + "/quickNotes/get?sorting=mDesc&startIndex=0&endIndex=10000000";
    private static final String URL_DOCUMENTS = BASIC_URL + "/document/get?sorting=mDesc&startIndex=0&endIndex=10000000";
    private static final String URL_CALENDAR_NOTES = BASIC_URL + "/calendars/notes/search?sorting=mDesc";
    private static final String URL_CALENDAR_NOTES_DATES_PATTERN = BASIC_URL + "/calendars/notes/%d/dates";

    public static void synchronizeNotes(String token) throws IOException {
        Thread saveNotes = new Thread(() -> {
            List<Note> notes = null;
            try {
                notes = receiveNotes(token);
                updateDatabase(notes);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        saveNotes.start();
    }

    private static void updateDatabase(List<Note> notes) {
        for (Note note : notes) {
            System.out.println(note);
        }
    }

    private static List<Note> receiveNotes(String token) throws InterruptedException {
        List<Note> allNotes = new ArrayList<>();
        Thread calendarNotesReceiver = new Thread(() -> {
            try {
                List<Note> notes = receiveCalendarNotes(token);
                allNotes.addAll(notes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        calendarNotesReceiver.start();


        calendarNotesReceiver.join();
        return allNotes;
    }


    private static List<Note> receiveCalendarNotes(String token) throws JSONException {
        List<Note> result = new ArrayList<>();

        JSONObject response = getResponse(URL_CALENDAR_NOTES, token);
        if (response != null) {
            JSONArray notes = response.getJSONArray("notes");
            for (int i = 0; i < notes.length(); i++) {
                Note note = new Note();

                JSONObject noteJson = notes.getJSONObject(i);
                int id = noteJson.getInt("id");
                String content = noteJson.getString("content");
                String title = noteJson.getString("title");
                String time = noteJson.getString("time");
                String modifyDate = noteJson.getString("modifyDate");
                String createDate = noteJson.getString("createDate");
                String calendar = (noteJson.getJSONObject("calendar")).getString("name");
                JSONArray tagsArray = noteJson.getJSONArray("tags");
                StringBuilder builder = new StringBuilder();
                for (int tagI = 0; tagI < tagsArray.length(); tagI++) {
                    builder.append(tagsArray.get(tagI));
                    builder.append(", ");
                }

                String tags = builder.toString();

                note.setId(id);
                note.setContent(content);
                note.setImageResource(R.drawable.icon_calendar_note);
                note.setHeader(title + " at " + time);
                note.setTags(tags);
                note.setPersistInfoDisplay("Create date: " + createDate + "\nModify date: " + modifyDate);

                JSONObject datesResponse = getResponse(String.format(URL_CALENDAR_NOTES_DATES_PATTERN, id), token);
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
}
