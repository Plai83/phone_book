import java.io.*;
import java.util.*;

public class PhoneBook {
    HashMap<String, ArrayList<Long>> fileTextList = new HashMap<>();
    Scanner iScanner = new Scanner(System.in);


    public HashMap<String, ArrayList<Long>> readFile() throws IOException {
//        Чтение записной книги
        BufferedReader reader = new BufferedReader(new FileReader("src/phonebook.txt"));
        String line = reader.readLine();

        while (line != null) {
            ArrayList<Long> tempArr = new ArrayList<>();
            for (String i: line.split(":")[1].strip().split(" ")) {
                tempArr.add(Long.valueOf(i));
            }
            fileTextList.put(line.split(":")[0], tempArr);
            line = reader.readLine();
        }
        System.out.println(fileTextList);
        reader.close();
        return fileTextList;
    }
    public void writeFile(HashMap<String, ArrayList<Long>> arrList) {
//        Обновление данных записной книги
        try (FileWriter fw = new FileWriter("src/phonebook.txt", false)) {
            for (var i: arrList.entrySet()) {
                fw.write(worldUpperCase(i.getKey()) + ":");
                for (Long k: i.getValue()) {
                    fw.append(String.valueOf(k));
                    fw.append(" ");
                }
                fw.append("\n");
                fw.flush();
            }
        } catch (IOException e) {
            System.out.println("Такого фала не существует");
        }
    }

    public String worldUpperCase(String text) {
//        Заглавные буквы имени и фамилии
        String[] textArr = text.split(" ");
        for (int i = 0; i < textArr.length; i++) {
            textArr[i] = textArr[i].substring(0, 1).toUpperCase() + textArr[i].substring(1);
        }
        return String.join(" ",textArr);
    }

    public void add(Boolean flag){
//    Добавление абонента
        ArrayList<Long> temp_arr = new ArrayList<>();
        System.out.println("Введите имя и фамилию: ");
        String name = worldUpperCase(iScanner.nextLine().toLowerCase());
        while (fileTextList.containsKey(name)) {
            System.out.println("Такой абонент уже существует введите данные другого абонента или " +
                    "'end' для выход в основное меню");
            name = worldUpperCase(iScanner.nextLine().toLowerCase());
            if ("end".equalsIgnoreCase(name)) {
                Collections.sort(temp_arr);
                return;
            }
        }

        while (true) {
            if (!flag) {
                System.out.println("Введите номер телефона или 'end' для возврата в главное меню:");
            } else {System.out.println("Введите номер телефона или 'end' для возврата в меню редактирования:");}
            String number = iScanner.nextLine();
            if (number.matches("[0-9]+") || "end".equalsIgnoreCase(number) ) {
                if ("end".equalsIgnoreCase(number)) {
                    Collections.sort(temp_arr);
                    fileTextList.put(name, temp_arr);
                    return;
                }
                temp_arr.add(Long.valueOf(number));
            } else {
                System.out.println("Телефон должен содержать только цифры");
            }

        }

    }
    public void del(){
//        Удаление абонента
        while (true) {
            System.out.println("Введите имя и фамилию абонента которого нужно удалить" +
                    " или 'end' для возврата в главное меню");
            String name = worldUpperCase(iScanner.nextLine().toLowerCase());
            if ("end".equalsIgnoreCase(name)) {
                return;
            }
            if (fileTextList.containsKey(name)) {
                fileTextList.remove(name);
                System.out.printf("Абонент %s удален", name);
                System.out.println();
            } else {
                System.out.println("Такого абонента в записях нет");
            }
        }
    }
    public void print(Boolean flag){
//        Вывод данных на печать
        String name = null;
        if (!flag) {
            System.out.println("Введите имя фамилию абонента которого надо распечатать, " +
                    "или 'all' если хотите вывести всех абонентов");
            name = worldUpperCase(iScanner.nextLine().toLowerCase());
        }
        if ("all".equalsIgnoreCase(name) || flag) {
            for (var i : fileTextList.entrySet()) {
                System.out.print(worldUpperCase(i.getKey()) + " ");
                for (var k : i.getValue()){
                    System.out.print(k + " ");
                }
                System.out.println();
            }
        } else {
            if (fileTextList.containsKey(name)) {
                System.out.print(name + " ");
                for (Long z: fileTextList.get(name)) {
                    System.out.print(z + " ");
                }
                System.out.println();
            } else {
                System.out.println("Такого абонента нет в книге");
            }
        }
    }
    public void edit(){
//        Редактирование записи
        while (true) {
            print(true);
            System.out.println("Введите имя или фамилию абонента которого надо изменить" +
                    " или 'end' для возврата в главное меню");
            String name = worldUpperCase(iScanner.nextLine().toLowerCase());
            if ("end".equalsIgnoreCase(name)) {
                return;
            }
            if (fileTextList.containsKey(name)) {
                fileTextList.remove(name);
                System.out.println("Введите новые данные");
                add(true);

            } else {
                System.out.println("Такого абонента в записях нет");
            }
        }
    }

    public void menuPhoneBook() throws IOException {
        HashMap<String, ArrayList<Long>> fileList = readFile();
        while (true) {
            System.out.println("Команды : \n 1. '+' Добавить нового абонента " +
                    "\n 2. 'print' вывести на экран записи" +
                    "\n 3. '-' удалить абонента" +
                    "\n 4. 'edit' изменить запись" +
                    "\n 5. 'Q' выйти из программы" +
                    "\n Введите команду: ");
            String command = iScanner.nextLine();

            switch (command.toLowerCase()) {
                case ("+"):
                    add(false);
                    break;
                case ("-"):
                    del();
                    break;
                case ("print"):
                    print(false);
                    break;
                case ("edit"):
                    edit();
                    break;
                case ("q"):
                    writeFile(fileList);
                    System.out.println("Записная книга обновлена");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Такой команды нет, введите команду еще раз");
            }
        }
    }
}