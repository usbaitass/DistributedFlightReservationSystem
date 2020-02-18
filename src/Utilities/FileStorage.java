package Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import Models.ServersList;
import com.google.gson.Gson;

/**
 * This class is for saving a data
 * 
 * @author SajjadAshrafCan
 *
 */
public class FileStorage {

	/**
	 * this method saves servers Configurations
	 * 
	 * @param serversConfiguration
	 * @return
	 */
	public String saveConfigFile(ServersList serverConfig) {
		String fileContent = getJsonFromObject(serverConfig);
		String filePath = (new File("App.Config")).getPath();
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(fileContent);
			fileWriter.flush();
			fileWriter.close();
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR : " + e.getMessage();
		}
	}

	/**
	 * this method opens up an Object from file
	 * 
	 * @param new_file
	 * @return
	 */
	public ServersList LoadServerConfigurations() {

		String fileContent = "";
		try {
			fileContent = new String(Files.readAllBytes(Paths.get((new File("App.Config")).getPath())));
			//fileContent = new String(File.readAllBytes(new File("App.Config")).getPath());
			return (ServersList) getObjectFromJson(fileContent, ServersList.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * this method gets json from object
	 * 
	 * @param new_object
	 *            new object
	 * @return json converts gson to json and returns it
	 */
	public String getJsonFromObject(Object new_object) {
		Gson gson = new Gson();
		return gson.toJson(new_object);
	}

	/**
	 * this methods gets object from a json
	 * 
	 * @param new_jsonString
	 *            json string object
	 * @param new_class
	 *            new clas
	 * @return object object from json
	 */
	public Object getObjectFromJson(String new_jsonString, Class<?> new_class) {
		Gson gson = new Gson();
		return gson.fromJson(new_jsonString, new_class);
	}

	/**
	 * read all data From File
	 * 
	 * @param path
	 * @param encoding
	 * @return
	 */
	public String readFromFile(String path, Charset encoding) {
		try {
			if (new File(path).exists()) {
				byte[] encoded = Files.readAllBytes(Paths.get(path));
				return new String(encoded, encoding);
			} else {
				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * Save whole string to file.
	 * 
	 * @param path
	 * @param content
	 * @return
	 */
	public boolean saveToFile(String path, String content) {
//		try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
//			out.print(content);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		
		return false;
	}

	/**
	 * Remove Character From Start or Left side
	 * 
	 * @param InputString
	 * @param Characters
	 * @return
	 */
	public String RemoveCharacterFromStrartorLeft(String InputString, String Characters) {
		return InputString.replaceAll("^\\" + Characters + "+", "");
	}

	/**
	 * Remove Character From End or Right
	 * 
	 * @param InputString
	 * @param Characters
	 * @return
	 */
	public String RemoveCharacterFromEndorRight(String InputString, String Characters) {
		return InputString.replaceAll("\\" + Characters + "+$", "");
	}

	/**
	 * Remove Character From Both End
	 * 
	 * @param InputString
	 * @param Characters
	 * @return
	 */
	public String RemoveCharacterFromBothEnd(String InputString, String Characters) {
		return InputString.replaceAll("^\\" + Characters + "+|\\" + Characters + "+$", "");
	}

}
