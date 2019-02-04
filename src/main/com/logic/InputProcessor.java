package main.com.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author gino.tkj@gmail.com
 */
public class InputProcessor {

    static Map<String, String> tokenRomanValueMapping = new HashMap<String, String>();
    static Map<String, Float> tokenIntegerValue = new HashMap<String, Float>();
    static Map<String, String> questionAndReply = new LinkedHashMap<String, String>();
    static ArrayList<String> missingValues = new ArrayList<String>();
    static Map<String, Float> elementValueList = new HashMap<String, Float>();

    /**
     * if file path is specified that is picked up else by default Input inside
     * the same package is pickedup. Each line is picked up and served to
     * processLine() for processing.
     *
     * @param filePath
     * @throws IOException
     */
    public static void ProcessFile(String filePath) throws IOException {
        BufferedReader bufferedReader = null;
        if (filePath == null) {
            InputStream in = InputProcessor.class.getResourceAsStream("Input");
            bufferedReader = new BufferedReader(new InputStreamReader(in));
        } else {
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
        }
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            processLine(line);
        }

        bufferedReader.close();
    }

    /**
     * processline adds the input to various maps<K,T> based on different
     * conditions.
     *
     * @param line
     */
    public static void processLine(String line) {
        String arr[] = line.split("((?<=:)|(?=:))|( )");

        if (line.endsWith("?")) {
            questionAndReply.put(line, "");
        } else if (arr.length == 3 && arr[1].equalsIgnoreCase("is")) {
            tokenRomanValueMapping.put(arr[0], arr[arr.length - 1]);
        } else if (line.toLowerCase().endsWith("credits")) {
            missingValues.add(line);
        }
    }

    /**
     * Maps tokens to Roman equivalent {pish=X, tegj=L, prok=V, glob=I}
     */
    public static void MapTokentoIntegerValue() {

        Iterator it = tokenRomanValueMapping.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry token = (Map.Entry) it.next();
            float integerValue = new RomanToDecimal().romanToDecimal(token.getValue().toString());
            tokenIntegerValue.put(token.getKey().toString(), integerValue);
        }
        mapMissingEntities();
    }

    /**
     * FInds the value of elements by decoding queries like [glob glob Silver is
     * 34 Credits]
     */
    private static void mapMissingEntities() {
        for (int i = 0; i < missingValues.size(); i++) {
            deCodeMissingQuery(missingValues.get(i));
        }
    }

    /**
     * Calculates the values of various elements and appends the same to map
     * elementValueList. elementValueList :{Gold=14450.0, Iron=195.5,
     * Silver=17.0}
     *
     * @param query
     */
    private static void deCodeMissingQuery(String query) {
        String array[] = query.split("((?<=:)|(?=:))|( )");
        int splitIndex = 0;
        int creditValue = 0;
        String element = null;
        String[] valueofElement = null;
        for (int i = 0; i < array.length; i++) {
            if (array[i].toLowerCase().equals("credits")) {
                creditValue = Integer.parseInt(array[i - 1]);
            }
            if (array[i].toLowerCase().equals("is")) {
                splitIndex = i - 1;
                element = array[i - 1];
            }
            valueofElement = java.util.Arrays.copyOfRange(array, 0, splitIndex);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < valueofElement.length; j++) {
            stringBuilder.append(tokenRomanValueMapping.get(valueofElement[j]));
        }
        float valueOfElementInDecimal = new RomanToDecimal().romanToDecimal(stringBuilder.toString());
        elementValueList.put(element, creditValue / valueOfElementInDecimal);
    }

}
