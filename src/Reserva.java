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

public class Reserva {
    static Scanner scanner = new Scanner(System.in);
    static List<Reserva> reservas = new ArrayList<>();

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

        System.out.println("Escolha o quarto para sua estadia:");
        System.out.println("1-Quarto Simples R$:100,00 | 2-Quarto Duplo R$:180,00| 3-Suíte Dupla R$:250,00|" +
                " 4-Suíte Presidencial R$:350,00");
        int quarto = scanner.nextInt();
        scanner.nextLine();

        double valorDiaria = 0.0;
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
        }

        System.out.println("Digite a data de Check-in(dd/MM/yyyy):");
        String entrada = scanner.nextLine();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            dateFormatter.parse(entrada);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
            return;
        }

        System.out.println("Digite a hora de Check-in(HH:mm)-(A diária é contabilizada à partir de 12:00h):");
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

        System.out.println("Digite a hora de Check-out(HH:mm)-(A diária é contabilizada à partir de 12:00h):");
        String saidaHora = scanner.nextLine();
        try {
            timeFormatter.parse(saidaHora);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de hora inválido. Use o formato HH:mm.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        LocalDateTime dataEntrada = LocalDateTime.parse(entrada + "-" + entradaHora, formatter);
        LocalDateTime dataSaida = LocalDateTime.parse(saida + "-" + saidaHora, formatter);

        Reserva reserva = new Reserva(cpf2,quarto,dataEntrada,dataSaida);
        reservas.add(reserva);

        long diasHospedagem = ChronoUnit.DAYS.between(dataEntrada.toLocalDate(), dataSaida.toLocalDate());

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
    }

    public static void cancelar() {
        if (reservas.isEmpty()) {
            System.out.println("Não há reservas para cancelar.");
            return;
        }
    
        System.out.println("Lista de reservas:");
        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            System.out.println((i + 1) + ". CPF: " + reserva.cpf + ", Quarto: " + reserva.quarto + ", Entrada: " + reserva.dataEntrada + ", Saída: " + reserva.dataSaida);
        }
    
        System.out.println("Digite o número da reserva que deseja cancelar:");
        int numeroReserva = scanner.nextInt();
        scanner.nextLine(); 
    
        if (numeroReserva < 1 || numeroReserva > reservas.size()) {
            System.out.println("Número de reserva inválido.");
            return;
        }
    
        Reserva reservaCancelada = reservas.remove(numeroReserva - 1);
        System.out.println("Reserva cancelada com sucesso:");
        System.out.println("CPF: " + reservaCancelada.cpf + ", Quarto: " + reservaCancelada.quarto + ", Entrada: " + reservaCancelada.dataEntrada + ", Saída: " + reservaCancelada.dataSaida);
    }

    public static void alterar() { 
        
        if (reservas.isEmpty()) {
            System.out.println("Não há reservas para alterar.");
            return;
        }
    
        System.out.println("Lista de reservas:");
        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            System.out.println((i + 1) + ". CPF: " + reserva.cpf + ", Quarto: " + reserva.quarto + ", Entrada: " + reserva.dataEntrada + ", Saída: " + reserva.dataSaida);
        }
    
        System.out.println("Digite o número da reserva que deseja alterar:");
        int numeroReserva = scanner.nextInt();
        scanner.nextLine();
    
        if (numeroReserva < 1 || numeroReserva > reservas.size()) {
            System.out.println("Número de reserva inválido.");
            return;
        }
    
        Reserva reservaSelecionada = reservas.get(numeroReserva - 1);
    
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
                    char novoQuarto = scanner.next().charAt(0);
                    scanner.nextLine();
                    reservaSelecionada.quarto = novoQuarto;
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
        System.out.println("CPF: " + reservaSelecionada.cpf + ", Quarto: " + reservaSelecionada.quarto + ", Nova Entrada: " + reservaSelecionada.dataEntrada + ", Nova Saída: " + reservaSelecionada.dataSaida);
    }
    
}
