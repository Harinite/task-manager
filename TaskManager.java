import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;



class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
}


class Task implements Comparable<Task>{

    private  static int id_counter = 0;
    public  int id = 0;
    public  String name;
    public  int priority;
    public   boolean status = false;

    public Task(){ // Конструктор по умолчанию
        this("Задача_по_умолчанию", 1);
    }

    public Task(String name, int priority){ // Конструктор для создания объекта класса Task
        this.name = name;
        this.priority = priority;
        this.id=++id_counter;
    }
    @Override 
    public int compareTo(Task other){ // Переопределяем compareTo для того чтобы наш PriorityQueue сравнивал объекты по полю priority в наших объектах класса Task
        return Integer.compare(this.priority, other.priority);
    }

    @Override 
    public String toString(){
        int W_ID = 5;
        int W_NAME = 77;
        int W_PRIORITY = 10;
        int W_STATUS = 15;
        String idPart = String.format("%-" + W_ID + "d", id);
        String namePart = String.format("%-" + W_NAME + "." + W_NAME + "s", name);
        String priorityPart = String.format("%-" + W_PRIORITY + "d", priority);
        String statusPart = String.format("%-" + W_STATUS + "s", status ? "✓ выполнена" : "✗ не выполнена");
        
        String coloredStatus = status ? Colors.GREEN + statusPart + Colors.RESET : Colors.RED + statusPart + Colors.RESET;
        
        return Colors.CYAN + "║ " + idPart + "║ " + namePart + "║ " + priorityPart + "║ " + coloredStatus + Colors.CYAN + " ║" + Colors.RESET;
        
    }

}






public class TaskManager {

    private  static int count_task = 0;
    private  static int count_complete = 0;
    private  static int count_unfuldilled = 0;
        
    public static void task_add(String task_name, int task_priority, PriorityQueue<Task> task_list){ //Добавление задачи с названием и приоритетом
        task_list.add(new Task(task_name, task_priority));
        count_task++;
        count_unfuldilled++;
        System.out.println(Colors.YELLOW + "Задача успешно добавлена!" + Colors.RESET);
    }

    public static void tasks_all(PriorityQueue<Task> task_list){ //Показать все задачи (по приоритету)
        if(task_list.isEmpty()){
            System.out.println(Colors.RED + "Список задач пока что пустой!" + Colors.RESET);
        }
        else{
            int width = 78;
            String line = "═".repeat(width);
            System.out.println(Colors.CYAN + "╔══════╦══════════════════════════════════════════════════════════════════════════════╦═══════════╦═════════════════╗" + Colors.RESET);
            System.out.println(Colors.CYAN + "║  id  ║ Название задачи                                                              ║ Приоритет ║ Статус          ║" + Colors.RESET);
            System.out.println(Colors.CYAN + "╠══════╬══════════════════════════════════════════════════════════════════════════════╬═══════════╬═════════════════╣" + Colors.RESET);
            task_list.stream().sorted().forEach(System.out::println);
            System.out.println(Colors.CYAN + "╚══════╩" + line + "╩═══════════╩═════════════════╝" + Colors.RESET);
        }
        
        
    }
    
    public static void task_mark_complete(int id_task, PriorityQueue<Task> task_list){//Отметить задачу выполненной 
        for(Task task : task_list){
            if(task.id == id_task){
                if(!task.status){
                    task.status = true;
                    count_complete++;
                    count_unfuldilled--;
                    System.out.println(Colors.YELLOW + "Задача с id " + id_task + " теперь стала выполненной" + Colors.RESET);
                }
                else{
                    System.out.println(Colors.RED + "задача с id " + id_task + " уже выполнена!" + Colors.RESET);
                }
                return;
            }
            
        }
        System.out.println(Colors.RED + "задача с id " + id_task + " не найдена!" + Colors.RESET);
        
    }

    public static void tasks_show_unfulfilled(PriorityQueue<Task> task_list){//Показать невыполненные задачи
        int width = 78;
        String line = "═".repeat(width);
        
        List<Task> found = task_list.stream().filter(task -> !task.status).sorted().collect(Collectors.toList());
        if(found.isEmpty()){
            System.out.println(Colors.RED + "Ничего не найдено!" + Colors.RESET);
        }
        else{
            System.out.println(Colors.CYAN + "╔══════╦══════════════════════════════════════════════════════════════════════════════╦═══════════╦═════════════════╗" + Colors.RESET);
            System.out.println(Colors.CYAN + "║  id  ║ Название задачи                                                              ║ Приоритет ║ Статус          ║" + Colors.RESET);
            System.out.println(Colors.CYAN + "╠══════╬══════════════════════════════════════════════════════════════════════════════╬═══════════╬═════════════════╣" + Colors.RESET);
            found.forEach(System.out::println);
            System.out.println(Colors.CYAN + "╚══════╩" + line + "╩═══════════╩═════════════════╝" + Colors.RESET);
        }
        
        
    }

    public static void task_delete(String name_task, PriorityQueue<Task> task_list){//Удалить задачу по названию
        for (Task task : task_list){
            if(task.name.equals(name_task)){
                if(task.status){
                    count_complete--;
                }
                else{
                    count_unfuldilled--;
                }
            }
        }

        boolean find_name_success = task_list.removeIf(task -> task.name.equals(name_task));
        if(find_name_success){
            count_task--;
            System.out.println(Colors.YELLOW + "Задача с названием '" + name_task + "' удалена" + Colors.RESET);
        }
        else{
            System.out.println(Colors.RED + "Задача с названием '" + name_task + "' не найдена" + Colors.RESET);
        }
    }

    public static void task_show_stat(PriorityQueue<Task> task_list){//Сколько всего, сколько выполнено, сколько не выполнено
        int width = 50;
        String line = "═".repeat(width);
    
        System.out.println(Colors.CYAN + "╔" + line + "╗" + Colors.RESET);
        System.out.println(Colors.CYAN + "║" + String.format("%-" + width + "s", "              СТАТИСТИКА ЗАДАЧ") + "║" + Colors.RESET);
        System.out.println(Colors.CYAN + "╠" + line + "╣" + Colors.RESET);
    
        System.out.println(Colors.CYAN + "║" + String.format("%-" + width + "s", " Всего задач:      " + count_task) + "║" + Colors.RESET);
    
        System.out.println(Colors.CYAN + "║" + Colors.RESET + Colors.GREEN + String.format("%-" + width + "s", " Выполнено:        " + count_complete) + Colors.CYAN + "║" + Colors.RESET);
    
        System.out.println(Colors.CYAN + "║" + Colors.RESET + Colors.RED + String.format("%-" + width + "s", " Не выполнено:     " + count_unfuldilled) + Colors.CYAN + "║" + Colors.RESET);
    
        System.out.println(Colors.CYAN + "╚" + line + "╝" + Colors.RESET);
    }

    public static void task_find_word(String word, PriorityQueue<Task> task_list){//Поиск задач по слову в названии
        int width = 78;
        String line = "═".repeat(width);
        
        List<Task> found = task_list.stream().filter(task -> task.name.contains(word)).sorted().collect(Collectors.toList());
        if(found.isEmpty()){
            System.out.println(Colors.RED + "Ничего не найдено!" + Colors.RESET);
        }
        else{
            System.out.println(Colors.CYAN + "╔══════╦══════════════════════════════════════════════════════════════════════════════╦═══════════╦═════════════════╗" + Colors.RESET);
            System.out.println(Colors.CYAN + "║  id  ║ Название задачи                                                              ║ Приоритет ║ Статус          ║" + Colors.RESET);
            System.out.println(Colors.CYAN + "╠══════╬══════════════════════════════════════════════════════════════════════════════╬═══════════╬═════════════════╣" + Colors.RESET);
            found.forEach(System.out::println);
            System.out.println(Colors.CYAN + "╚══════╩" + line + "╩═══════════╩═════════════════╝" + Colors.RESET);
        }
        
    }


    public static void start_programm(PriorityQueue<Task> task_list){
        Scanner scanner = new Scanner(System.in);
        boolean program_status = true;
        while (program_status) {
            System.out.println(Colors.GREEN + "╔══════════════════════════════════════════════════════════╗" + Colors.RESET);
            System.out.println(Colors.GREEN + "║                 МЕНЕДЖЕР ЗАДАЧ v1.0                      ║" + Colors.RESET);
            System.out.println(Colors.GREEN + "╠══════════════════════════════════════════════════════════╣" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  1. Показать все задачи") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  2. Добавить задачу") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  3. Отметить выполненной") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  4. Показать невыполненные") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  5. Удалить по названию") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  6. Статистика") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  7. Поиск по слову") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "║" + String.format("%-58s", "  0. Выход") + "║" + Colors.RESET);
            System.out.println(Colors.GREEN + "╚══════════════════════════════════════════════════════════╝" + Colors.RESET);
            System.out.print(Colors.GREEN + "Выберите действие: "  + Colors.RESET);

            int user_action = scanner.nextInt();
            scanner.nextLine();
            System.out.println(Colors.YELLOW + "Выбрано действие " + user_action + Colors.RESET);
            switch (user_action) {
                case 0:
                    program_status = false;
                    break;
                case 1:
                    tasks_all(task_list);
                    break;
                case 2:
                    System.out.println(Colors.GREEN + "Введите название задачи:" + Colors.RESET);
                    String name_task = scanner.nextLine();
                    System.out.println(Colors.GREEN + "Введите приоритет задачи:" + Colors.RESET);
                    user_action = scanner.nextInt();
                    scanner.nextLine();
                    task_add(name_task, user_action, task_list);
                    break;    
                case 3:
                    System.out.println(Colors.GREEN + "Введите id задачи:" + Colors.RESET);
                    user_action = scanner.nextInt();
                    scanner.nextLine();
                    task_mark_complete(user_action, task_list);
                    break;
                case 4:
                    tasks_show_unfulfilled(task_list);
                    break;
                case 5:
                    System.out.println(Colors.GREEN + "Введите название задачи:" + Colors.RESET);
                    name_task = scanner.nextLine();
                    task_delete(name_task, task_list);
                    break;
                case 6:
                    task_show_stat(task_list);
                    break;
                case 7:
                    System.out.println(Colors.GREEN + "Введите слово из названия задачи:" + Colors.RESET);
                    String word_name_task = scanner.nextLine();
                    task_find_word(word_name_task, task_list);
                    break;
            
                default:
                    break;
            }

        }
    }

    public static void main(String[] args) {
        PriorityQueue<Task> list = new PriorityQueue<>();
        start_programm(list);
        
    }
}