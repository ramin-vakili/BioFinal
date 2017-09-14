package generate;

import java.util.ArrayList;
import java.util.Random;


public class SequenceHelper {

    public static String generateSequence(ArrayList<BaseOption> baseOptions) {

        StringBuilder sequence = new StringBuilder();
        BaseOption randomOption;
        Random r = new Random();

        while (baseOptions.size() > 0) {
            int optionPos = r.nextInt(baseOptions.size());
            randomOption = baseOptions.get(optionPos);
            sequence.append(randomOption.getPattern());
            randomOption.decreaseNum();
            if (randomOption.getNum() <= 0) {
                baseOptions.remove(optionPos);
            }
        }
        return sequence.toString();
    }

    public static ArrayList<String> split(int min, int max, String sequence, int coverage) {

        ArrayList<String> MainResult = new ArrayList<>();

        for (int i = 0; i < coverage; i++) {
            int start = 0;
            int end = 0;
            int length = sequence.length();
            ArrayList<String> subResults = new ArrayList<>();

            Random random = new Random();
            int randomLength;
            while (start < length) {
                randomLength = (random.nextInt(max - min + 1)) + min;
                end = start + randomLength;
                String temp;
                if (end > length || (length - end) < min) {
                    end = length;
                }

                temp = sequence.substring(start, end);
                start = end;
                subResults.add(temp);
            }
            MainResult.addAll(subResults);
            subResults.clear();
        }
        return MainResult;
    }
    
    public static ArrayList<String> spiltV2(int min, int max, String sequence, int coverage) {
        ArrayList<String> MainResult = new ArrayList<>();
        ArrayList<Integer> cuts = new ArrayList<>();

        for (int i = 0; i < coverage; i++) {
            int start = 0;
            int end = 0;
            int length = sequence.length();
            ArrayList<String> subResults = new ArrayList<>();

            Random random = new Random();
            int randomLength;
            while (start < length) {
                randomLength = (random.nextInt(max - min + 1)) + min;
                end = start + randomLength;
                if(!cuts.contains(end) && !cuts.contains(end+1) && !cuts.contains(end+2) && !cuts.contains(end+3) && !cuts.contains(end+4) ){
                    cuts.add(end);
                    String temp;
                    if (end > length || (length - end) < min) {
                        end = length;
                    }
                    temp = sequence.substring(start, end);
                    start = end;
                    subResults.add(temp);
               }
            }
            MainResult.addAll(subResults);
            subResults.clear();
        }
        return MainResult;
    }

}
