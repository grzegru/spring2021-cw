package pl.javastart.todo;

import org.springframework.stereotype.Controller;
import pl.javastart.todo.dto.NewTaskDto;
import pl.javastart.todo.dto.TaskDurationDto;
import pl.javastart.todo.exception.TaskALreadyCompletedException;
import pl.javastart.todo.exception.TaskAlreadyStartedException;
import pl.javastart.todo.exception.TaskNotFoundException;
import pl.javastart.todo.exception.TaskNotStartedException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

@Controller
class TaskController {
    private final TaskService taskService;
    private final Scanner scanner;

    public TaskController(TaskService taskService, Scanner scanner) {
        this.taskService = taskService;
        this.scanner = scanner;
    }

    public void loop() {
        Option option;
        do {
            printOptions();
            option = chooseOption();
            evaluateOption(option);
        } while (option != Option.EXIT);
    }

    private void printOptions() {
        System.out.println("\nWybierz opcję:");
        for (Option option : Option.values()) {
            System.out.println(option);
        }
    }

    private Option chooseOption() {
        int optionNumber = scanner.nextInt();
        scanner.nextLine();
        return Option.fromInt(optionNumber);
    }

    private void evaluateOption(Option option) {
        try{
            switch (option) {
                case ADD -> {
                    addTask();
                }
                case PRINT_SINGLE -> {
                    printTask();
                }
                case START_TASK -> {
                    startTask();
                }
                case END_TASK -> {
                    endTask();
                }
                case EXIT -> {
                    exit();
                }
            }
        }catch (TaskNotFoundException e){
            System.out.println("Nie znaleziono zadania o podanym id");
        }

    }






    private void addTask() {
        System.out.println("Podaj tytuł zadania:");
        String title = scanner.nextLine();
        System.out.println("Opis zadania:");
        String description = scanner.nextLine();
        System.out.println("Priorytet (wyższa liczba = wyższy priorytet):");
        int priority = scanner.nextInt();
        scanner.nextLine();
        NewTaskDto task = new NewTaskDto(title, description, priority);
        Long savedTaskId = taskService.saveTask(task);
        System.out.println("Zadanie zapisane z identyfikatorem " + savedTaskId);
    }

    private void startTask() {
        System.out.println("Podaj id zadania, które chcesz wystartować");
        long id = scanner.nextLong();
        scanner.nextLine();
        try{
            LocalDateTime taskStartTime = taskService.startTask(id);
            System.out.println("Czas rozpoczecia zadania: " + taskStartTime);
        }catch (TaskAlreadyStartedException e){
            System.out.println("Zadanie już zostało wcześniej wystartowane");
        }

    }


    private void endTask() {
        System.out.println("Podaj id zadanie, które chcesz zakończyć");
        long id = scanner.nextLong();
       scanner.nextLine();
       try{
           TaskDurationDto taskDuration = taskService.completeTask(id);
           System.out.println(taskDuration);
       }catch (TaskALreadyCompletedException e){
           System.out.println("Zadanie zostało już wcześniej zakończone");
       }catch (TaskNotStartedException e){
           System.out.println("Zadanie nie zostało jeszcze rozpoczęte");
       }
    }

    private void printTask() {
        System.out.println("Podaj identyfikator zadania:");
        long taskId = scanner.nextLong();
        scanner.nextLine();
        taskService.getTaskInfo(taskId)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Brak wpisu o takim id")
                );
    }

    private void exit() {
        System.out.println("Koniec programu!");
    }

    private enum Option {
        ADD(1, "Dodaj nowe zadanie"),
        PRINT_SINGLE(2, "Wyświetl zadanie"),
        START_TASK(3, "Wystartuj zadanie"),
        END_TASK(4, "Zakończ zadanie"),
        EXIT(5, "Koniec programu");

        private final int number;
        private final String name;

        Option(int number, String name) {
            this.number = number;
            this.name = name;
        }

        static Option fromInt(int option) {
            return values()[option - 1];
        }

        @Override
        public String toString() {
            return number + " - " + name;
        }
    }
}
