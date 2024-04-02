import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        System.out.println("Digite a hora de Check-in(HH:mm)-(A diária é contabilizada à partir de 12:00h):");
        String entradaHora = scanner.nextLine();
        System.out.println("Digite a data de Check-out(dd/MM/yyyy):");
        String saida = scanner.nextLine();
        System.out.println("Digite a hora de Check-out(HH:mm)-(A diária é contabilizada à partir de 12:00h):");
        String saidaHora = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        LocalDateTime dataEntrada = LocalDateTime.parse(entrada+entradaHora,formatter);
        LocalDateTime dataSaida = LocalDateTime.parse(saida+saidaHora,formatter);

        Reserva reserva = new Reserva(cpf2,quarto,dataEntrada,dataSaida);
        reservas.add(reserva);

    }

    public static void cancelar() {
    }

    public static void alterar() {

    }
}
