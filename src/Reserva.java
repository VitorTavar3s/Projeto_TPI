import java.util.Scanner;

public class Reserva {
    static Scanner scanner = new Scanner(System.in);

    public static void reservar() {
        //CPF do hóspede, escolha do quarto , tempo de permanência
        System.out.println("Digite o CPF do hóspede:");
        String cpf = scanner.nextLine();
        for (Hospede hospede : Main.hospedes) {
            if (!hospede.getCpf().equals(cpf)) {
                System.out.println("CPF não cadastrado. Cadastre o hóspede primeiro.\n");
                return;
            }
        }
        System.out.println("Escolha o quarto para sua estadia:");
        System.out.println("1-Quarto Simples | 2-Quarto Duplo | 3-Suíte Dupla | 4-Suíte Presidencial");
        char quarto = scanner.next().charAt(0);
        System.out.println("Digite a data de Check-in:");
        String entrada = scanner.nextLine();
        System.out.println("Digite a data de Check-out:");
        String saida = scanner.nextLine();
    }

    public static void cancelar() {
    }

    public static void alterar() {

    }
}
