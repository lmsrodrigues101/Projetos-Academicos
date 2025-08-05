import java.io.*;
import java.util.Scanner;
import dataStructures.*;
import Exceptions.*;
import TrainStation.*;

/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */


public class Main {
    private static final String AUCTION_FILE = "store.dat";
    private static final String PERCURSO_IMPOSSIVEL = "Percurso impossível.";
    private static final String ESTAÇAO_PARTIDA_INEXISTENTE = "Estação de partida inexistente.";
    private static final String LINHA_INEXISTENTE = "Linha inexistente.";
    private static final String LINHA_EXISTENTE = "Linha existente.";
    private static final String INSERÇAO_LINHA_SUCESSO = "Inserção de linha com sucesso.";
    private static final String REMOÇAO_LINHA_SUCESSO = "Remoção de linha com sucesso.";
    private static final String HORARIO_INVALIDO = "Horário inválido.";
    private static final String CRIAÇAO_HORARIO_SUCESSO = "Criação de horário com sucesso.";
    private static final String HORARIO_INEXISTENTE = "Horário inexistente.";
    private static final String ESTAÇAO_INEXISTENTE = "Estação inexistente.";
    private static final String REMOÇAO_HORARIO_SUCESSO = "Remoção de horário com sucesso.";
    private static final String SPLIT = " ";
    private static final String EMPTY = "";
    private static final String COLON = ":";
    private static final String ZERO = "0";
    private static final String COMBOIO = "Comboio ";


     private enum Commands {
        IL, RL, CL, IH, RH, CH, MH, TA, INVALID, CE, LC
    }


    public static void main(String[] args) {
        RailNetwork network = load();
        executeCmd(network);
        save(network);

    }

    private static void save(RailNetwork network) {
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(AUCTION_FILE));
            oos.writeObject(network);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {

            throw new RuntimeException(e);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private static RailNetwork load() {
        try {
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(AUCTION_FILE));
            RailNetwork network = (RailNetwork) ois.readObject();
            ois.close();
            return network;
        } catch (IOException e) {
            return new RailNetworkClass();

        } catch (ClassNotFoundException e) {
            return new RailNetworkClass();
        }
    }

    private static void executeCmd(RailNetwork network) {
        Scanner in = new Scanner(System.in);
        Commands option;
        do{
            option = getCommand(in);
            selectCmd(in,network,option);
        }while(!option.equals(Commands.TA));
        in.close();
    }

    private static void selectCmd(Scanner in, RailNetwork network, Commands option) {
        switch (option) {
            case TA -> System.out.println("Aplicação terminada.");
            case IL -> insertLine(in, network);
            case RL -> removeLine(in, network);
            case CL -> consultStations(in, network);
            case CE -> consultLine(in,network);
            case IH -> insertTime(in,network);
            case RH -> removeTime(in, network);
            case CH -> consultTimeOfLine(in, network);
            case MH -> bestTime(in, network);
            case LC -> consultSchedulesByStation(in, network);
            default -> {
            }
        }
    }




    private static Commands getCommand(Scanner in) {
        try {
            String comm = in.next().toUpperCase();
            return Commands.valueOf(comm);
        } catch (IllegalArgumentException e) {
            return Commands.INVALID;
        }
    }
    private static void insertLine(Scanner in, RailNetwork network) {
        String nameLine = in.nextLine().trim();
        Queue<String> queue = new QueueInList<>();
        String nameStation = in.nextLine().trim();
        try {
            while (!nameStation.isEmpty()) {
                queue.enqueue(nameStation);
                nameStation = in.nextLine().trim();
            }
            network.addLine(nameLine, queue);
            System.out.println(INSERÇAO_LINHA_SUCESSO);
        } catch (AlreadyExistLineException e) {
            System.out.println(LINHA_EXISTENTE);
        }
    }

    private static void removeLine(Scanner in, RailNetwork network) {
        String nameLine = in.nextLine().trim();
        try {
            network.removeLine(nameLine);
            System.out.println(REMOÇAO_LINHA_SUCESSO);
        }catch (LineDoesNotExistException e) {
            System.out.println(LINHA_INEXISTENTE);
        }
    }

    private static void consultStations(Scanner in, RailNetwork network) {
        String nameLine = in.nextLine().trim();
        try {
            Iterator<Station> itStations = network.getStationsIterator(nameLine);
            while(itStations.hasNext()){
                StationGets station = itStations.next();
                System.out.println(station.getName());
            }
        }catch (LineDoesNotExistException e) {
            System.out.println(LINHA_INEXISTENTE);

        }
    }

    private static void consultLine(Scanner in, RailNetwork network) {
        String stationName = in.nextLine().trim();
        try {
            Iterator<Entry<Line,Line>> itLines = network.getLinesIterator(stationName);
            while(itLines.hasNext()){
                LineGets line = itLines.next().getKey();
                System.out.println(line.getName());
            }
        }catch (StationDoesNotExistException e){
            System.out.println(ESTAÇAO_INEXISTENTE);
        }
     }

     private static void insertTime(Scanner in, RailNetwork network) {
        String nameLine = in.nextLine().trim();
        int trainId = in.nextInt();
        in.nextLine();
        Queue<Entry<String,String>> schedule = new QueueInList<>();
        try{
            String stringInput;
            do{
                stringInput = in.nextLine().trim();
                if(!stringInput.isEmpty()) {
                    String[] tokens = stringInput.split(SPLIT);
                    StringBuilder stationName = new StringBuilder();
                    for (int i = 0; i < tokens.length - 1; i++) {
                        if (i > 0) stationName.append(SPLIT);
                        stationName.append(tokens[i]);
                    }
                    String stationFinalName = stationName.toString();
                    String time = tokens[tokens.length - 1];
                    Entry<String, String> entryOfSchedule = new EntryClass<>(stationFinalName, time);
                    schedule.enqueue(entryOfSchedule);
                }
            }while(!stringInput.isEmpty());

            network.insertTime(nameLine,trainId,schedule);
            System.out.println(CRIAÇAO_HORARIO_SUCESSO);
        }catch (LineDoesNotExistException e){
            System.out.println(LINHA_INEXISTENTE);
        }catch (InvalidScheduleException e){
            System.out.println(HORARIO_INVALIDO);
        }
    }
    
    private static void removeTime(Scanner in, RailNetwork network) {
        String nameLine = in.nextLine().trim();
        try {
            String stringInput = in.nextLine();
            String[] tokens = stringInput.split(SPLIT);
            StringBuilder nameStation = new StringBuilder(EMPTY);
            for( int i = 0; i < tokens.length - 1; i++ ) {
                nameStation.append(tokens[i]).append(SPLIT);
            }
            nameStation = new StringBuilder(nameStation.toString().trim());
            String time = tokens[tokens.length - 1];
            network.removeTime(nameLine, nameStation.toString(),time);
            System.out.println(REMOÇAO_HORARIO_SUCESSO);
        }catch (LineDoesNotExistException e) {
            System.out.println(LINHA_INEXISTENTE);
        }catch (ScheduleDoesNotExistException e){
            System.out.println(HORARIO_INEXISTENTE);
        }
    }
    private static void consultTimeOfLine(Scanner in, RailNetwork network) {
        String lineName = in.nextLine().trim();
        String startStation = in.nextLine().trim();
        try {
            Iterator<Entry<Time,Schedule>> schedules = network.getSchedulesOfStartStation(lineName,startStation);
            while(schedules.hasNext()) {
                Entry<Time,Schedule> schedule = schedules.next();
                System.out.println(schedule.getValue().getTrainId());
                Iterator<Entry<Station,Time>> routes = schedule.getValue().getEntries();
                while(routes.hasNext()) {
                    Entry<Station,Time> route = routes.next();
                    System.out.print(route.getKey().getName() + SPLIT);
                    printTime(route.getValue());
                }
            }
        }catch(LineDoesNotExistException e){
            System.out.println(LINHA_INEXISTENTE);
        }catch(StartStationInexistentException e){
            System.out.println(ESTAÇAO_PARTIDA_INEXISTENTE);
        }
    }
    
    private static void consultSchedulesByStation(Scanner in, RailNetwork network) {
        String stationName = in.nextLine().trim();
        try {
            Iterator<Entry<Integer, Time>> it = network.getSchedulesByStation(stationName);
            while (it.hasNext()) {
                Entry<Integer, Time> schedule = it.next();
                System.out.print(COMBOIO + schedule.getKey() + SPLIT);
                printTime(schedule.getValue());
            }
        } catch (StationDoesNotExistException e) {
            System.out.println(ESTAÇAO_INEXISTENTE);
        }
    }

    private static void bestTime(Scanner in, RailNetwork network) {
        String lineName = in.nextLine().trim();
        String startStation = in.nextLine().trim();
        String endStation = in.nextLine().trim();
        String expArrivalTime = in.nextLine().trim();
        try{
            Schedule bestTimeSchedule = network.getBestTime(lineName,startStation,endStation,expArrivalTime);
            System.out.println(bestTimeSchedule.getTrainId());
            Iterator<Entry<Station,Time>> bestEntry = bestTimeSchedule.getEntries();
            while(bestEntry.hasNext()){
                Entry<Station,Time> entry = bestEntry.next();
                System.out.print(entry.getKey().getName() + SPLIT);
                printTime(entry.getValue());
            }
        } catch (LineDoesNotExistException e) {
            System.out.println(LINHA_INEXISTENTE);
        } catch (NonExistentDepartureStationException e) {
            System.out.println(ESTAÇAO_PARTIDA_INEXISTENTE);
        } catch (ImpossibleRouteException e) {
            System.out.println(PERCURSO_IMPOSSIVEL);
        }
    }

    private static void printTime(Time time){
         if(time.getHours() < 10 && time.getMinutes() < 10){
             System.out.println(ZERO + time.getHours() + COLON + ZERO + time.getMinutes());
         }else if(time.getHours() < 10 && time.getMinutes() >= 10){
             System.out.println(ZERO + time.getHours() + COLON + time.getMinutes());
         }else if(time.getHours() >= 10 && time.getMinutes() < 10){
             System.out.println(time.getHours() + COLON + ZERO + time.getMinutes());
         }else {
             System.out.println(time.getHours() + COLON + time.getMinutes());
         }
     }
     
}