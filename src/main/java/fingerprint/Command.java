package fingerprint;

import com.google.gson.Gson;
import sourceafis.FingerprintMatcher;
import sourceafis.FingerprintTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Command extends DatabaseConnection
{
    private int start = 1;
    private String command_name;

    private static String dbDriver = "mysql";
    private static String Table = "persons";

    /*
     * Show application Title and welcome
     */
    public void show_project_title_with_welcome(){
        System.out.println(" ");
        System.out.println("********************************************************************");
        System.out.println(" ............Welcome to Finger Print Identifier Application............ ");
        System.out.println(" .....................Created by Digitalistic...................... ");
        System.out.println(" ------------------------------------------------------------------- ");
    }


    /*
     * Show all application command
     */
    public void showCommands(){
        System.out.println(" ................Application Command help ................");
        System.out.println(" Command List ..................... About Description");
        System.out.println(" ........................................");
        System.out.println(" 'help'   ...........................  application command help");
        System.out.println(" 'exit'   ...........................  close application");
        System.out.println(" 'enroll' ...........................  enroll person");
        System.out.println(" 'find'   ..........................   find person");
        System.out.println("------------------------------------------------------------");
    }



    /*
     * run application command for action
     */
    public void runCommands() throws IOException, SQLException {
        while(start > 0){
            Scanner inputCommand = new Scanner(System.in);
            System.out.print(">> ");
            command_name = inputCommand.next();

            switch(command_name){
                case "help":
                    showCommands();
                    break;
                case "enroll":
                    Scanner inputPerson = new Scanner(System.in);
                    System.out.print("ID : ");
                    String nationalId = inputPerson.nextLine();
                    System.out.print("Name Bangla : ");
                    String nameBangla = inputPerson.nextLine();
                    System.out.print("Name English : ");
                    String nameEnglish = inputPerson.nextLine();
                    System.out.print("Father Name : ");
                    String fatherName = inputPerson.nextLine();
                    System.out.print("Mother Name : ");
                    String motherName = inputPerson.nextLine();
                    System.out.print("Date of Birth : ");
                    String dob = inputPerson.nextLine();
                    System.out.print("Address : ");
                    String address = inputPerson.nextLine();
                    System.out.print("Finger Print Path : ");
                    String imagePath = inputPerson.nextLine();

                    Person person = new Person();
                    person.setNationalId(nationalId);
                    person.setNameBangla(nameBangla);
                    person.setNameEnglish(nameEnglish);
                    person.setFatherName(fatherName);
                    person.setMotherName(motherName);
                    person.setDOB(dob);
                    person.setAddress(address);
                    person.setTemplate(generateTempalateJson(imagePath));
                    enrollPerson(person);
                    show_messge_for_command();
                    break;
                case "find":
                    Scanner findPerson = new Scanner(System.in);
                    String person_find = findPerson.next();
                    Person personData = findPerson(person_find);
                    showPersonInfo(personData);
                    show_messge_for_command();
                    break;
                case "exit":
                    start=0;
                    break;
                default:
                    System.out.println("Know how to use application ? please Type 'help' for Application command help");
                    break;
            }
        }
    }


    /*
     * show message for action
     */
    private void show_messge_for_command(){
        System.out.println("Please Enter your next Command to execute action");
    }


    public String generateTempalateJson(String imagePath) throws IOException
    {
        byte[] probeImage = Files.readAllBytes(Paths.get(imagePath));
        FingerprintTemplate probeTemplate = new FingerprintTemplate(probeImage, 500);
        String fingerPrint = probeTemplate.toJson();
        return fingerPrint;
    }


    /*
     * Enroll Person to DB
     */
    public int enrollPerson(Person person)
    {
        int result = 0;
        try {
            Connection DB = DatabaseConnection.dbConnection(dbDriver);
            String query = "insert into persons (national_id,name_bangla,name_english,father_name,mother_name,dob,address,finger_print) values (?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = DB.prepareStatement(query);
            pstmt.setString(1, person.getNationalId());
            pstmt.setString(2, person.getNameBangla());
            pstmt.setString(3, person.getNameEnglish());
            pstmt.setString(4, person.getFatherName());
            pstmt.setString(5, person.getMotherName());
            pstmt.setString(6, person.getDOB());
            pstmt.setString(7, person.getAddress());
            pstmt.setString(8, String.valueOf(person.getTemplate()));
            pstmt.executeUpdate();
            result = 1;
            DB.close();
            System.out.println("data save");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("data not save");
        }

        return result;
    }


    /*
     * Find Person is DB
     */
    public Person findPerson(String imagePath) throws IOException, SQLException
    {
        byte[] probeImage = Files.readAllBytes(Paths.get(imagePath));
        FingerprintTemplate probe = new FingerprintTemplate(probeImage, 500);
        FingerprintMatcher matcher = new FingerprintMatcher(probe);
        Person match = null;
        double high = 0;
        Connection DB = DatabaseConnection.dbConnection(dbDriver);

        try {
            String query = "select * from " + Table;
            PreparedStatement pstmt = DB.prepareStatement(query);
            ResultSet pr = pstmt.executeQuery();

            while (pr.next())
            {
                Person person = new Person();
                person.setId(pr.getInt(1));
                person.setNationalId(pr.getString(2));
                person.setNameBangla(pr.getString(3));
                person.setNameEnglish(pr.getString(4));
                person.setFatherName(pr.getString(5));
                person.setMotherName(pr.getString(6));
                person.setDOB(pr.getString(7));
                person.setAddress(pr.getString(8));
                person.setTemplate(pr.getString(9));
                person.setPhoto(pr.getString(10));

                FingerprintTemplate template = FingerprintTemplate.fromJson(person.getTemplate());
                double score = matcher.match(template);

                if (score > high) {
                    high = score;
                    match = person;
                }
            }

            double threshold = 40;
            return high >= threshold ? match : null;

        }catch(Exception e){
            System.out.println("Error Message = " + e.getMessage());
            return null;
        }finally {
            DB.close();
        }
    }


    public void showPersonInfo(Person person)
    {
        if(person == null){
            System.out.println("person not found");
            System.out.println("");
        }else{
            System.out.println("National ID : " + person.getNationalId());
            System.out.println("Name Bangla : " + person.getNameBangla());
            System.out.println("Name English : " + person.getNameEnglish());
            System.out.println("Father Name : " + person.getFatherName());
            System.out.println("Mother Name : " + person.getMotherName());
            System.out.println("Date of Birth : " + person.getDOB());
            System.out.println("Address : " + person.getAddress());
            System.out.println("Photo : " + person.getPhoto());
            System.out.println("json : " + toJson(person));
            System.out.println(toJson(person));
        }
    }


    public String toJson(Person person)
    {
        return new Gson().toJson(person);
    }





}
