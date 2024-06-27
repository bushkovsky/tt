package org.bushkovsky;

import com.garmin.fit.*;

import java.io.FileInputStream;

public class FitFileReader implements ReadPort {

    public DtoTrackInfo read(String path) {

        try (FileInputStream fitFileInputStream = new FileInputStream(path)) {
            // Create a decoder for the .fit file
            Decode decode = new Decode();

            // Create a MesgBroadcaster to receive messages from the .fit file
            MesgBroadcaster mesgBroadcaster = new MesgBroadcaster(decode);

            // Create a listener to process the messages
            FitDataListener fitDataListener = new FitDataListener();
            mesgBroadcaster.addListener((RecordMesgListener) fitDataListener);

            // Read the file
            if (!decode.checkFileIntegrity(fitFileInputStream)) {
                throw new RuntimeException("FIT file integrity failed.");
            }

            fitFileInputStream.getChannel().position(0);

            decode.read(fitFileInputStream, mesgBroadcaster, mesgBroadcaster);

            return fitDataListener.getTrackInfo();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
