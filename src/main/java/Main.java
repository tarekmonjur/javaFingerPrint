
import fingerprint.Command;
import fingerprint.Person;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        Command command = new Command();

//     D:\\environments\\java\\fingerprint\\src\\main\\java\\probe.png // test image file loc

        if(args.length > 0){
            for(int i=0;i< args.length;i++)
            {
                if(args[i].equals("enroll"))
                {
                    Person person = new Person();
                    person.setNationalId(args[1]);
                    person.setNameBangla(args[2]);
                    person.setNameEnglish(args[3]);
                    person.setFatherName(args[4]);
                    person.setMotherName(args[5]);
                    person.setDOB(args[6]);
                    person.setAddress(args[7]);
                    person.setTemplate(command.generateTempalateJson(args[8]));
                    command.enrollPerson(person);
                }else if(args[i].equals("find")){
                    Person personData = command.findPerson(args[1]);
                    command.showPersonInfo(personData);
                }
            }
        }else{
            command.show_project_title_with_welcome();
            command.showCommands();
            command.runCommands();
        }





    }

}
