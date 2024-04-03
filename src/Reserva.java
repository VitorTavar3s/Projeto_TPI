import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reserva {
    static Scanner scanner = new Scanner(System.in);
    static List<Reserva> reservas = new ArrayList<>();

    String cpf;
    char quarto;
    LocalDateTime dataEntrada,dataSaida;

    public Reserva(String cpf,char quarto, LocalDateTime dataEntrada, LocalDateTime dataSaida) {
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
                break; // Encerra o loop assim que encontrar o CPF cadastrado
            }
        }

        if (!cpfCadastrado) {
            System.out.println("CPF não cadastrado. Cadastre o hóspede primeiro.\n");
            return; // Sai do método em caso de CPF não cadastrado
        }

        System.out.println("Escolha o quarto para sua estadia:");
        System.out.println("1-Quarto Simples | 2-Quarto Duplo | 3-Suíte Dupla | 4-Suíte Presidencial");
        char quarto = scanner.next().charAt(0);
        scanner.nextLine();

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

        System.out.println("Reserva cadastrada com sucesso!");

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
    }
}
