package eg.tedyoung.springproxies.ex5;


public class ProcessingService {
	Processor processor;
	
	public ProcessingService(Processor processor) {
		this.processor = processor;
	}

	public void process(String data) {
		processor.process(data);
	}
}
