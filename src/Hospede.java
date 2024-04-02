import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hospede {
    static Scanner scanner = new Scanner(System.in);
    String cpf,nome;
    static String arquivo = "hospede.csv";

    public Hospede(String cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public static void adicionarHospede() {
        System.out.println("Digite seu CPF(Apenas números):");
        String cpf = scanner.nextLine();
        for (Hospede hospede : Main.hospedes){
            if(hospede.getCpf().equals(cpf)){
                System.out.println("CPF já cadastrado. Não é possível cadastrar novamente.\n");
                return;
            }
        }
        System.out.println("Digite seu nome:");
        String nome = scanner.nextLine();

        Hospede hospede = new Hospede(cpf,nome);
        Main.hospedes.add(hospede);
        Hospede.salvarHospedeArquivo(Main.hospedes);
        Hospede.lerHospedeArquivo();
    }

    public static void salvarHospedeArquivo(List<Hospede> hospedes){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("hospede.csv"))){
                writer.write("CPF;Nome");
                writer.newLine();
            for (Hospede hospede : hospedes){
                writer.write(hospede.getCpf()+";"+hospede.getNome());
                writer.newLine();
            }
            System.out.println("Arquivo salvo com sucesso!");
        }catch (IOException e){
            System.err.println("Erro ao salvar no arquivo: "+e.getMessage());
        }
    }

    public static List<Hospede> lerHospedeArquivo(){
       try {
           Stream<String> stream = Files.lines(Paths.get("hospede.csv"));
           List<Hospede> hospedes =
                   stream.skip(1)
                   .map(Hospede::mapear)
                   .collect(Collectors.toList());
           stream.close();
           return hospedes;
       }catch (IOException e){
           System.out.println(e);
           return null;
       }
    }

    public static Hospede mapear(String linha) {
        String[] partes = linha.split(";");
        String cpf = partes[0];
        String nome = partes[1];
        return new Hospede(cpf,nome);
    }

}
