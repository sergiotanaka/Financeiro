package domain.exercicio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RepositorioExercicio {
	private static final String FILE_NAME = "exercicio.dat";

	public void inserir(Exercicio exercicio) throws IOException {
		FileOutputStream arquivoGrav = new FileOutputStream(FILE_NAME);

		// Classe responsavel por inserir os objetos
		ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);

		// Grava o objeto cliente no arquivo
		objGravar.writeObject(exercicio);
		objGravar.flush();
		objGravar.close();
		arquivoGrav.flush();
		arquivoGrav.close();
	}

	public Exercicio recuperar() throws IOException, ClassNotFoundException {
		// Carrega o arquivo
		FileInputStream arquivoLeitura = new FileInputStream(FILE_NAME);

		// Classe responsavel por recuperar os objetos do arquivo
		ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);

		Exercicio exercicio = (Exercicio) objLeitura.readObject();

		objLeitura.close();
		arquivoLeitura.close();

		return exercicio;
	}

}
