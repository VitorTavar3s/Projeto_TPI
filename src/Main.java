import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Hospede> hospedes = Hospede.lerHospedeArquivo();

    static List<Reserva> reservas = Reserva.lerReservasArquivo();

    public static void main(String[] args) {
        //Check-in - Check-out de um hotel
        //RESERVAR - Recebe o nome e cpf(identificador) do hóspede / Escolhe o quarto / Escolhe a data de entrada e de saída.
        //CANCELAR - Apaga a reserva.
        //ALTERAR - Altera a data de entrada ou saída, altera o quarto escolhido.

        int opcao;


        do {
            System.out.println("################################################");
            System.out.println("######## ADA Hotel - Reserva de Quartos ########");
            System.out.println("################################################\n");


            System.out.println(">>>> Menu <<<<");
            System.out.println("1 - Adicionar Hóspede");
            System.out.println("2 - Reservar Quarto");
            System.out.println("3 - Cancelar Reserva");
            System.out.println("4 - Alterar Reserva");
            System.out.println("5 - Sair");

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> Hospede.adicionarHospede();
                case 2 -> Reserva.reservar();
                case 3 -> Reserva.cancelar();
                case 4 -> Reserva.alterar();
                case 5 -> System.out.println("Saindo...");
                default -> System.out.println("Opção " + opcao + " é inválida, Digite 1, 2, 3, 4 ou 5! \n");
            }
        } while (opcao != 5);
    }
}