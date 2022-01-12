//  Uses Google Cloud Vision API to load an iMessage GamePigeon avalanche mancala game directly from a screenshot
//  NOTE: run GOOGLE_APPLICATION_CREDENTIALS must be set before running:
//  set GOOGLE_APPLICATION_CREDENTIALS=D:\Users\Ez3d\Documents\Cloud Vision\mancala-1639878949959-d9b6e998c679.json

package mancala;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadGame {

    public static int[] loadGame(String filePath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        // Load image
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(imgBytes).build();

        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        // Sets language to korean (improves number detection accuracy)
        ImageContext context = ImageContext.newBuilder().addLanguageHints("ko").build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img)
                .setImageContext(context).build();
        requests.add(request);

        // Initialize client to send API requests
        ImageAnnotatorClient client = ImageAnnotatorClient.create();
        BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        // Interpret responses
        ArrayList<Integer> nums = new ArrayList<Integer>();
        for (AnnotateImageResponse res : responses) {
            for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                if (isNumeric(annotation.getDescription()))
                    nums.add(Integer.parseInt(annotation.getDescription()));
            }
        }

        client.close();

        // Adds numbers to a mancala board int array
        // Cycles in ascending order
        // 0 13 12 11 10 9 8 7
        // 0 1 2 3 4 5 6 7
        int[] board = new int[15];
        for (int i = 1; i <= 6; i++) {
            board[i] = nums.get(2 * i - 2);
            board[14 - i] = nums.get(2 * i - 1);
        }
        // 0 is P2 mancala, 7 is P1 mancala.
        board[0] = 0;
        board[7] = 0;
        // 14 is free turn tracker
        board[14] = 0;

        return board;
    }

    // Checks if a string is numeric
    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
