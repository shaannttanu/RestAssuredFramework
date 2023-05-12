package Globals;

import client.GetConfigProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class AllGlobals {
    public static void setGlobalVariable(String variableName, String value) throws Exception{

        FileReader reader = new FileReader(GetConfigProperties.getGlobalVariableJsonFilePath());
        BufferedReader bufferedReader = new BufferedReader(reader);
        String json = bufferedReader.readLine();

        // Convert the JSON string into a JSON object
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // Update the JSON object
        ObjectNode objectNode = (ObjectNode) rootNode;
        objectNode.put(variableName, value);

        // Convert the JSON object back to a JSON string
        String updatedJson = objectMapper.writeValueAsString(rootNode);

        // Write the updated JSON string back to the file
        FileWriter writer = new FileWriter(GetConfigProperties.getGlobalVariableJsonFilePath());
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(updatedJson);
        bufferedWriter.close();

    }

    public static String getGlobalVariable(String variableName) throws Exception{
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(GetConfigProperties.getGlobalVariableJsonFilePath()));
        JSONObject jsonObject = (JSONObject) obj;

        return (String)jsonObject.get(variableName);
    }
}