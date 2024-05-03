// programa: votaciones en un parlamento
// versión: 20240503
// autor: Carlos Grasa Lambea

import java.util.HashMap;
import java.util.Iterator;

public class Parlamento { // registra votos a grupos parlamentarios
	// campos
	private static final HashMap<String, String> partidos=new HashMap<>(); // mapa con clave y {s|n}entero como string


	public static void main(String[] args) { // método principal
		char opcion; // contiene la opción del menú elegida por el usuario
		do { // repite mientras usuario no decida salir
			K.escribir("\n".repeat(40)); // borra la pantalla
			K.escribir("--------------- menú del sistema de votación --------------\n");
			K.escribir("- Listar los datos introducidos\n");
			K.escribir("- Introducir votos\n");
			K.escribir("- Desempatar votación\n");
			K.escribir("- Borrar todos los registros\n");
			K.escribir("- Salir del programa\n");
			K.escribir("-----------------------------------------------------------\n");
			opcion = K.preguntarChar("Elija una opción", "lidbs"); // pide opción de menú
			switch (opcion) {
				case 'l' -> listarDatos(); // muestra todos los grupos y sus votos
				case 'i' -> pedirDatos(false); // pide datos de votación para los grupos
				case 'd' -> desempatar(); // pide datos sólo para los marcados como ganadores
				case 'b' -> borrarDatos(); // borra todos los registros
				case 's' -> K.escribir("Terminando programa.\n"); // termina el programa
			}
		} while(opcion!='s');
		return;
	}
	private static void pedirDatos(boolean esDesempate) { // pide datos a introducir, para todos o sólo ganadores
		String respuesta; // respuesta del usuario por consola
		String clave; // nombre del grupo a recibir votos
		String registro; // registro del grupo; consta de un carácter 's' o 'n' seguido de un entero de los votos
		int votosAnteriores; // votos recibidos previamente
		int votos; // votos a sumar
		boolean esActivo; // está marcado o no como ganador a desempatar
		K.escribir("\n".repeat(40)); // borra la pantalla
		K.escribir("--------------- introduciendo datos de votación --------------\n");
		respuesta=normalizarTexto(K.preguntar("Introduzca nombre de grupo (INTRO=fin)")); // pide clave de registro
		while (!respuesta.equals("")) { // mientras haya entrada de dato
			clave=respuesta; // recoger nombre del votando
			K.escribir("Grupo \"" + clave.toUpperCase() + "\""); // mostrar nombre del votando
			if (partidos.containsKey(clave)) { // si ya existía en registro...
				K.escribir(" ya estaba registrado"); // indicar ya registrado
				registro=partidos.get(clave); // obtener datos del votando
				esActivo=registro.charAt(0)=='s'; // desglosar la marca de ganador
				votosAnteriores=Integer.parseInt(registro, 1, registro.length(), 10); // recoger el número registrado de votos
			} else { // si no...
				K.escribir(" no estaba registrado"); // indicar nuevo en registro
				votosAnteriores=0; // asignar 0 votos
				esActivo=false; // marcar como no ganador
			}
			if (esDesempate && !esActivo) { // si es votación de desempate y no es ganador...
				K.escribir(" y ahora no es votable.\n"); // no permite votación
			} else { // si no...
				K.escribir(" y ahora sí es votable.\n"); // indicar votación permitida
				K.escribir("Antes tenía "+votosAnteriores+" votos.\n"); // mostrar votos previos
				votos=(int) K.preguntarLong("Introduzca los votos a añadir"); // pedir votos a añadir
				votos+=votosAnteriores; // sumar votos previos a nuevos
				if (votos<0) // si votos es negativo...
					votos=0; // establecer mínimo en cero
				registro=(esActivo ? "s" : "n")+votos; // formar el registro concatenando marca de ganador con número de votos
				partidos.remove(clave); // dar de baja el partido si existía previamente
				partidos.put(clave, registro); // dar de alta el partido con datos actualizados
				K.escribir("Grupo \"" + clave.toUpperCase() + "\" ahora tiene "+votos+" votos y "); // mostrar datos de votos
				K.escribir("está marcado como "+(esActivo ? "activo" : "inactivo")+".\n"); // mostrar marca de ganador
				K.escribir("--------------------------------------\n");
			}
			respuesta=normalizarTexto(K.preguntar("Introduzca nombre de grupo (INTRO=fin)")); // pide clave de registro
		} // fin de bucle de petición de datos
		return;
	}
	public static String normalizarTexto(String texto) { // acondiciona la clave en minúsculas sin espacios dobles...
		String normalizando=texto.strip().toLowerCase(); // quita espacios iniciales y finales y pasa a minúsculas
		while (normalizando.contains("  "))
			normalizando=normalizando.replace("  ", " "); // quita espacios dobles
		return normalizando;
	}
	public static void listarDatos() { // muestra todos los registros de datos
		boolean hayEmpate=false; // de momento no se ha detectado empate
		String registro; // string para datos
		String ganador=""; // nombre de ganador
		int votos; // número de votos del registro
		int votosMax=Integer.MIN_VALUE; // máximo número de votos encontrado hasta ahora
		K.escribir("\n".repeat(40)); // borrar pantalla
		K.escribir("--------------- listado del estado de la votación --------------\n");
		if (partidos.isEmpty()) { // si no hay datos...
			K.escribir("No existen registros para mostrar.\n"); // indicar que no hay datos
		} else { // si sí que hay datos...
			for (String clave: partidos.keySet()) { // para cada una de las claves en registros
				registro=partidos.get(clave); // obtener los datos del registro
				votos=Integer.parseInt(registro, 1, registro.length(), 10); // obtener número de votos registrados
				if (votos>votosMax) { // si nuevo récor de votos...
					votosMax = votos; // anotar récor
					ganador=clave; // anotar nombre de ganador
					hayEmpate=false; // deshacer empate
				} else if (votos==votosMax) // si no hay nuevo récor pero se iguala...
					hayEmpate=true; // ...entonces hay empate
				K.escribir("Grupo \""+clave.toUpperCase()+"\" tiene "+votos+" votos.\n"); // mostrar nombre y votos
			}
			if (hayEmpate) { // si al final había empates...
				K.escribir("-------------------------------------------------\n");
				K.escribir("Hay empate entre varios grupos con "+votosMax+" votos.\n"); // ...indicar empates
			} else // si no había empates...
				K.escribir("No existe empate. El ganador es \""+ganador.toUpperCase()+"\" con "+votosMax+" votos.\n"); // ...mostrar ganador
		}
		K.escribir("-------------------------------------------------\n");
		K.preguntar("Pulse INTRO para volver al menú"); // pausar programa para poder leer la pantalla
		return;
	}
	public static void borrarDatos() { // eliminar todos los registros
		K.escribir("--------------- boorando todos los registros de votación --------------\n");
		partidos.clear(); // borrar el contenido del hashmap
		K.escribir("Todos los registros se han borrado.\n"); // indicar borrado con éxito
		K.preguntar("Pulse INTRO para volver al menú"); // pausa para leer
		return;
	}
	public static void desempatar() { // realizar desempate entre ganadores
		boolean hayEmpate=false; // de momento no hay empate detectado
		String registro; // string para datos del registro
		String ganador=""; // string para nombre de un ganador
		int votos; // número de votos
		int votosMax=Integer.MIN_VALUE; // número récor de votos
		if (partidos.isEmpty()) // si no hay datos...
			K.escribir("No existen registros.\n"); // ...indicar que no hay datos
		else { // si sí que hay datos
			for (String clave : partidos.keySet()) { // para cada partido
				registro = partidos.get(clave); // obtener sus datos registrados
				votos = Integer.parseInt(registro, 1, registro.length(), 10); // obtener los votos registrados
				if (votos > votosMax) { // si hay nuevo récor...
					votosMax = votos; // anotar nuevo récor
					ganador=clave; // apuntar nombre de ganador
					hayEmpate = false; // deshacer empate
				} else if (votos == votosMax) // si récor igualado
					hayEmpate = true; // indicar empate
			}
			if (hayEmpate) { // si al final se ha detectado empate
				K.escribir("-------------------------------------------------\n");
				K.escribir("Hay empate entre varios grupos con " + votosMax + " votos.\n"); // indicar que hay empate
				K.escribir("Debe introducir más votos a los ganadores."); // aconsejar más votaciones
				K.escribir("Estos son los grupos con máxima puntuación:\n");
				for (String clave : partidos.keySet()) { // buscar en todos los registros
					registro = partidos.get(clave);
					votos = Integer.parseInt(registro, 1, registro.length(), 10);
					if (votos == votosMax) { // si es un ganador...
						registro="s"+votos;
						partidos.replace(clave, registro); // ...marcar registro como ganador...
						K.escribir("- Grupo \"" + clave.toUpperCase() + "\"\n"); // ...y mostrar nombre de ganador
					}
				}
				K.preguntar("Pulse INTRO para introducir más votos a los ganadores."); // avisar que se van a pedir más votos
				pedirDatos(true); // pide datos sólo para los ganadores empatados
			} else // si no había empate
				K.escribir("No existe empate. El ganador es \""+ganador.toUpperCase()+"\" con "+votosMax+" votos.\n"); // mostrar ganador único
		}
		K.preguntar("Pulse INTRO para volver al menú"); // pausa para leer la pantalla
		return;
	}
}
// fin del programa
