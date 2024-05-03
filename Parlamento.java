// programa: votaciones en un parlamento
// versión: 20240503
// autor: Carlos Grasa Lambea

import java.util.HashMap;
import java.util.Iterator;

public class Parlamento {
	// campos
	private static final HashMap<String, String> partidos=new HashMap<>(); // mapa con clave y {s|n}entero como string


	public static void main(String[] args) {
		char opcion;
		do {
			K.escribir("\n".repeat(40));
			K.escribir("--------------- menú del sistema de votación --------------\n");
			K.escribir("- Listar los datos introducidos\n");
			K.escribir("- Introducir votos\n");
			K.escribir("- Desempatar votación\n");
			K.escribir("- Borrar todos los registros\n");
			K.escribir("- Salir del programa\n");
			K.escribir("-----------------------------------------------------------\n");
			opcion = K.preguntarChar("Elija una opción", "lidbs");
			switch (opcion) {
				case 'l' -> listarDatos();
				case 'i' -> pedirDatos(false);
				case 'd' -> desempatar();
				case 'b' -> borrarDatos();
				case 's' -> K.escribir("Terminando programa.\n");
			}
		} while(opcion!='s');
		return;
	}
	private static void pedirDatos(boolean esDesempate) { // pide datos a introducir, para todos o sólo seleccionados
		String respuesta;
		String clave;
		String registro;
		int votosAnteriores;
		int votos;
		boolean esActivo;
		K.escribir("\n".repeat(40));
		K.escribir("--------------- introduciendo datos de votación --------------\n");
		respuesta=normalizarTexto(K.preguntar("Introduzca nombre de grupo (INTRO=fin)"));
		while (!respuesta.equals("")) {
			clave=respuesta;
			K.escribir("Grupo \"" + clave.toUpperCase() + "\"");
			if (partidos.containsKey(clave)) {
				K.escribir(" ya estaba registrado");
				registro=partidos.get(clave);
				esActivo=registro.charAt(0)=='s';
				votosAnteriores=Integer.parseInt(registro, 1, registro.length(), 10);
			} else {
				K.escribir(" no estaba registrado");
				votosAnteriores=0;
				esActivo=false;
			}
			if (esDesempate && !esActivo) {
				K.escribir(" y ahora no es votable.\n");
			} else {
				K.escribir(" y ahora sí es votable.\n");
				K.escribir("Antes tenía "+votosAnteriores+" votos.\n");
				votos=(int) K.preguntarLong("Introduzca los votos a añadir");
				votos+=votosAnteriores;
				if (votos<0)
					votos=0;
				registro=(esActivo ? "s" : "n")+votos;
				partidos.remove(clave);
				partidos.put(clave, registro);
				K.escribir("Grupo \"" + clave.toUpperCase() + "\" ahora tiene "+votos+" votos y ");
				K.escribir("está marcado como "+(esActivo ? "activo" : "inactivo")+".\n");
				K.escribir("--------------------------------------\n");
			}
			respuesta=normalizarTexto(K.preguntar("Introduzca nombre de grupo (INTRO=fin)"));
		}
		return;
	}
	public static String normalizarTexto(String texto) {
		String normalizando=texto.strip().toLowerCase();
		while (normalizando.contains("  "))
			normalizando=normalizando.replace("  ", " ");
		return normalizando;
	}
	public static void listarDatos() {
		boolean hayEmpate=false;
		String registro;
		int votos;
		int votosMax=0;
		K.escribir("\n".repeat(40));
		K.escribir("--------------- listado del estado de la votación --------------\n");
		if (partidos.isEmpty()) {
			K.escribir("No existen registros para mostrar.\n");
		} else {
			for (String clave: partidos.keySet()) {
				registro=partidos.get(clave);
				votos=Integer.parseInt(registro, 1, registro.length(), 10);
				if (votos>votosMax) {
					votosMax = votos;
					hayEmpate=false;
				} else if (votos==votosMax)
					hayEmpate=true;
				K.escribir("Grupo \""+clave.toUpperCase()+"\" tiene "+votos+" votos.\n");
			}
			if (hayEmpate) {
				K.escribir("-------------------------------------------------\n");
				K.escribir("Hay empate entre varios grupos con "+votosMax+" votos.\n");
			}
		}
		K.escribir("-------------------------------------------------\n");
		K.preguntar("Pulse INTRO para volver al menú");
		return;
	}
	public static void borrarDatos() {
		K.escribir("--------------- boorando todos los registros de votación --------------\n");
		partidos.clear();
		K.escribir("Todos los registros se han borrado.\n");
		K.preguntar("Pulse INTRO para volver al menú");
		return;
	}
	public static void desempatar() {
		return;
	}
}