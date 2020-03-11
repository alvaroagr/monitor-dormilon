import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Estudiante extends Thread{
	
	int id;								// Identificador del estudiante
	Queue<Integer> corredor;			// Cola del corredor. Maximo 3 personas pueden estar en ella.
										//  Solo un estudiante/monitor puede leer/modificarla a la vez.
	Semaphore sCorredor;				// Semaforo usado para que solo uno lea/escriba sobre la cola.
	Semaphore sMonitor;					// Semaforo usado para que el monitor atienda al siguiente (y se despierte)
	Semaphore sMonitoria;				// Semaforo usado para informar al estudiante actual que se puede ir.
	Random genAleat;					// Random para los tiempos programando en sala.
	
	public Estudiante(int id, Queue corredor, Semaphore sCorredor, Semaphore sMonitor, Semaphore sMonitoria, long seed) {
		super();
		this.id = id;
		this.corredor = corredor;
		this.sCorredor = sCorredor;
		this.sMonitor = sMonitor;
		this.sMonitoria = sMonitoria;
		this.genAleat = new Random(seed);
	}
	
	public void run() {
		while(true) {
			try {
				sleep(Math.abs(genAleat.nextInt()) % 1000);
				System.out.println("[ESTUDIANTE " + id + "] Necesito ayuda. Voy a donde el monitor.");
				sCorredor.acquire();
				if(corredor.size() == 0) {
					System.out.println("[ESTUDIANTE " + id + "] No hay sillas libres. Voy a programar en sala y vuelvo luego.");
					sCorredor.release();
				} else {
					corredor.poll();
					System.out.println("[ESTUDIANTE " + id + "] Estoy en fila. Quedan "+corredor.size()+" sillas disponibles.");
					sCorredor.release();
					sMonitor.release();
					sMonitoria.acquire();
					System.out.println("[ESTUDIANTE " + id + "] Termine la monitoria. Voy a programar en sala.");
				}
				
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	

}
