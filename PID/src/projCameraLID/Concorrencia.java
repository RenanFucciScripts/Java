package projCameraLID;

public class Concorrencia implements Runnable {

	@SuppressWarnings({ "static-access" })
	public void run(){
		//Instancia um thread à uma determinada classe
		Thread threadCameraUm = new Thread(new CameraUm()); 
//		static Thread threadCameraDois = new Thread(new CameraDois());


		//Thread para o tempo

		Thread threadTempo = new Thread();
		threadTempo.start();
		int ite=0;

		while (true) {
			try {
				if(ite>0){
					threadCamUm(threadCameraUm);
					//threadCamDois(threadCameraDois);
					ite+=1;
				}
				else{
					threadTempo.sleep(minutos(1)); //1 min = 60 000 millis
					System.out.println("Reiniciando após o tempo ");
					threadCamUm(threadCameraUm);
					//threadCamDois(threadCameraDois);
				}
			} catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
			}
		}



	}
	public static int minutos(int min){
		min=( min* 60000);
		return min;
	}

	public static void threadCamUm(Thread threadCameraUm){
		threadCameraUm.interrupt(); //Interrompe o thread
		System.gc(); //Chama o Garbage Collection
		threadCameraUm = new  Thread(new CameraUm()); 
		threadCameraUm.start();
	}

	public static void threadCamDois(Thread threadCameraDois){
		threadCameraDois.interrupt(); //Interrompe o thread
		System.gc();//Chama o Garbage Collection
		threadCameraDois = new Thread( new CameraDois());
		threadCameraDois.start();
	}
	

}
