import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reserva {
    static Scanner scanner = new Scanner(System.in);

    String cpf;
    int quarto;
    LocalDateTime dataEntrada,dataSaida;

    public Reserva(String cpf,int quarto, LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        this.cpf = cpf;
        this.quarto = quarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public static void reservar() {
        System.out.println("Lista de hóspedes cadastrados:");
        for (Hospede hospede : Main.hospedes) {
            System.out.println("CPF: " + hospede.getCpf() + ", Nome: " + hospede.getNome());
        }
        
        //CPF do hóspede, escolha do quarto , tempo de permanência
        System.out.println("Digite o CPF do hóspede:");
        String cpf2 = scanner.nextLine();
        boolean cpfCadastrado = false;

        for (Hospede hospede : Main.hospedes) {
            if (hospede.getCpf().equals(cpf2)) {
                cpfCadastrado = true;
                break;
            }
        }

        if (!cpfCadastrado) {
            System.out.println("CPF não cadastrado. Cadastre o hóspede primeiro.\n");
            return; // Sai do método em caso de CPF não cadastrado
        }

        int quarto = 0;
        double valorDiaria = 0.0;
        do {
            System.out.println("Escolha o quarto para sua estadia:");
            System.out.println("1-Quarto Simples R$:100,00 | 2-Quarto Duplo R$:180,00| 3-Suíte Dupla R$:250,00|" +
                    " 4-Suíte Presidencial R$:350,00|");

            quarto = scanner.nextInt();
            scanner.nextLine();
            switch (quarto){
                case 1:
                    valorDiaria = 100.00;
                    break;
                case 2:
                    valorDiaria = 180.00;
                    break;
                case 3:
                    valorDiaria = 250.00;
                    break;
                case 4:
                    valorDiaria = 350.00;
                    break;
                default: System.out.println("Quarto " + quarto + " é inválido, Digite 1, 2, 3 ou 4 \n");
            }
        }while (quarto > 4 || quarto < 1);

        System.out.println("Digite a data de Check-in(dd/MM/yyyy):");
        String entrada = scanner.nextLine();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            dateFormatter.parse(entrada);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
            return;
        }

        LocalDate dataAtual = LocalDate.now();
        LocalDate dataCheckIn = LocalDate.parse(entrada, dateFormatter);

        if (dataCheckIn.isBefore(dataAtual)) {
            System.out.println("A data de check-in não pode ser anterior à data de hoje!");
            return;
        }

        System.out.println("Digite a hora de Check-in(HH:mm):");
        String entradaHora = scanner.nextLine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            timeFormatter.parse(entradaHora);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de hora inválido. Use o formato HH:mm.");
            return;
        }

        System.out.println("Digite a data de Check-out(dd/MM/yyyy):");
        String saida = scanner.nextLine();
        try {
            dateFormatter.parse(saida);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
            return;
        }

        LocalDate dataCheckOut = LocalDate.parse(saida, dateFormatter);

        if (dataCheckOut.isBefore(dataCheckIn)) {
            System.out.println("A data de check-out não pode ser anterior à data de check-in.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        LocalDateTime dataEntrada = LocalDateTime.parse(entrada + "-" + entradaHora, formatter);

        System.out.println("Digite a hora de Check-out(HH:mm):");
        String saidaHora = scanner.nextLine();
        try {
            timeFormatter.parse(saidaHora);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de hora inválido. Use o formato HH:mm.");
            return;
        }

        LocalDateTime dataSaida = LocalDateTime.parse(saida + "-" + saidaHora, formatter);

        Reserva reserva = new Reserva(cpf2,quarto,dataEntrada,dataSaida);
        Main.reservas.add(reserva);

        long diasHospedagem = ChronoUnit.DAYS.between(dataEntrada.toLocalDate(), dataSaida.toLocalDate());
        if (dataSaida.toLocalDate().isEqual(dataEntrada.toLocalDate())){
            diasHospedagem = 1;
        }

        double totalReserva = valorDiaria * diasHospedagem;

        DateTimeFormatter formatterDiaSemana = DateTimeFormatter.ofPattern("EEEE",
                new Locale("pt", "BR"));

        String diaSemanaEntrada = formatterDiaSemana.format(dataEntrada);
        String diaSemanaSaida = formatterDiaSemana.format(dataSaida);

        System.out.println();
        System.out.println("Reserva cadastrada com sucesso! \n" +
                "Você ficará hospedado por " + diasHospedagem + " dias, check-in dia: " + entrada +
                " " +diaSemanaEntrada + " e check-out dia: " + saida + " " +diaSemanaSaida + "! \n" +
                "O valor total da hospedagem é de R$" + totalReserva);
        Reserva.salvarReservaArquivo(Main.reservas);

    }
    public static void salvarReservaArquivo(List<Reserva> reservas){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("reserva.csv"))){
            writer.write("CPF;Quarto;DataEntrada;DataSaida");
            writer.newLine();
            for(Reserva reserva : reservas){
                String linha = reserva.cpf + ";" + reserva.quarto + ";" +
                        reserva.dataEntrada + ";" + reserva.dataSaida;
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Reservas salvas com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar no arquivo reserva.csv"+ e.getMessage());
        }
    }
    public static List<Reserva> lerReservasArquivo(){
        try {
            Stream<String> stream = Files.lines(Paths.get("reserva.csv"));
            List<Reserva> reservas =
                    stream.skip(1)
                            .filter(linha -> !linha.trim().isEmpty())
                            .map(Reserva::mapear)
                            .collect(Collectors.toList());
            stream.close();
            return reservas;
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo"+ e.getMessage());
            return new ArrayList<>(); // Retorna uma lista vazia caso ocorra erros
        }

    }
    public static Reserva mapear(String linha){
        String[] partes = linha.split(";");
        String cpf = partes[0];
        int quarto = Integer.parseInt(partes[1]);
        LocalDateTime dataEntrada = null;
        LocalDateTime dataSaida = null;

        if (!partes[2].isEmpty()) {
            dataEntrada = LocalDateTime.parse(partes[2]);
        }

        if (!partes[3].isEmpty()) {
            dataSaida = LocalDateTime.parse(partes[3]);
        }
        return new Reserva(cpf,quarto,dataEntrada,dataSaida);
    }
    public static void cancelar() {
        if (Main.reservas.isEmpty()) {
            System.out.println("Não há reservas para cancelar.");
            return;
        }
    
        System.out.println("Lista de reservas:");
        for (int i = 0; i < Main.reservas.size(); i++) {
            Reserva reserva = Main.reservas.get(i);
            System.out.println((i + 1) + ". CPF: " + reserva.cpf + ", Quarto: " + reserva.quarto +
                    ", Entrada: " + reserva.dataEntrada + ", Saída: " + reserva.dataSaida);
        }
    
        System.out.println("Digite o número da reserva que deseja cancelar:");
        int numeroReserva = scanner.nextInt();
        scanner.nextLine(); 
    
        if (numeroReserva < 1 || numeroReserva > Main.reservas.size()) {
            System.out.println("Número de reserva inválido.");
            return;
        }
    
        Reserva reservaCancelada = Main.reservas.remove(numeroReserva - 1);
        System.out.println("Reserva cancelada com sucesso:");
        System.out.println("CPF: " + reservaCancelada.cpf + ", Quarto: " + reservaCancelada.quarto +
                ", Entrada: " + reservaCancelada.dataEntrada + ", Saída: " + reservaCancelada.dataSaida);

        Reserva.salvarReservaArquivo(Main.reservas);
        Reserva.lerReservasArquivo();
    }

    public static void alterar() { 
        
        if (Main.reservas.isEmpty()) {
            System.out.println("Não há reservas para alterar.");
            return;
        }
    
        System.out.println("Lista de reservas:");
        for (int i = 0; i < Main.reservas.size(); i++) {
            Reserva reserva = Main.reservas.get(i);
            System.out.println((i + 1) + ". CPF: " + reserva.cpf + ", Quarto: " + reserva.quarto +
                    ", Entrada: " + reserva.dataEntrada + ", Saída: " + reserva.dataSaida);
        }
    
        System.out.println("Digite o número da reserva que deseja alterar:");
        int numeroReserva = scanner.nextInt();
        scanner.nextLine();
    
        if (numeroReserva < 1 || numeroReserva > Main.reservas.size()) {
            System.out.println("Número de reserva inválido.");
            return;
        }
    
        Reserva reservaSelecionada = Main.reservas.get(numeroReserva - 1);
    
        boolean continuarAlterando = true;
    
        do {
            System.out.println("Escolha o que deseja alterar:");
            System.out.println("1. Tipo de quarto");
            System.out.println("2. Data do check-in");
            System.out.println("3. Data do check-out");
            System.out.println("4. Horário do check-in");
            System.out.println("5. Horário do check-out");
    
            int opcao = scanner.nextInt();
            scanner.nextLine();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
            switch (opcao) {
                case 1:
                    System.out.println("Escolha o novo tipo de quarto:");
                    System.out.println("1-Quarto Simples | 2-Quarto Duplo | 3-Suíte Dupla | 4-Suíte Presidencial");
                    reservaSelecionada.quarto = scanner.nextInt();
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.println("Digite a nova data de Check-in (dd/MM/yyyy):");
                    String novaDataCheckIn = scanner.nextLine();
                    try {
                        LocalDate novaEntradaDate = LocalDate.parse(novaDataCheckIn, dateFormatter);
                        reservaSelecionada.dataEntrada = reservaSelecionada.dataEntrada.with(novaEntradaDate);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Digite a nova data de Check-out (dd/MM/yyyy):");
                    String novaDataCheckOut = scanner.nextLine();
                    try {
                        LocalDate novaSaidaDate = LocalDate.parse(novaDataCheckOut, dateFormatter);
                        reservaSelecionada.dataSaida = reservaSelecionada.dataSaida.with(novaSaidaDate);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
                        return;
                    }
                    break;
                case 4:
                    System.out.println("Digite o novo horário do Check-in (HH:mm):");
                    String novoHorarioCheckIn = scanner.nextLine();
                    try {
                        LocalTime novoCheckInTime = LocalTime.parse(novoHorarioCheckIn, timeFormatter);
                        reservaSelecionada.dataEntrada = reservaSelecionada.dataEntrada.with(novoCheckInTime);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de horário inválido. Use o formato HH:mm.");
                        return;
                    }
                    break;
                case 5:
                    System.out.println("Digite o novo horário do Check-out (HH:mm):");
                    String novoHorarioCheckOut = scanner.nextLine();
                    try {
                        LocalTime novoCheckOutTime = LocalTime.parse(novoHorarioCheckOut, timeFormatter);
                        reservaSelecionada.dataSaida = reservaSelecionada.dataSaida.with(novoCheckOutTime);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de horário inválido. Use o formato HH:mm.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
    
            System.out.println("Deseja fazer mais alguma alteração? (S/N)");
            String resposta = scanner.nextLine();
            if (!resposta.equalsIgnoreCase("S")) {
                continuarAlterando = false;
            }
    
        } while (continuarAlterando);
    
        System.out.println("Reserva alterada com sucesso:");
        System.out.println("CPF: " + reservaSelecionada.cpf + ", Quarto: " +
                reservaSelecionada.quarto + ", Nova Entrada: " + reservaSelecionada.dataEntrada +
                ", Nova Saída: " + reservaSelecionada.dataSaida);

        Reserva.salvarReservaArquivo(Main.reservas);
        Reserva.lerReservasArquivo();
    }

}
