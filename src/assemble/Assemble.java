package assemble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import sabe.MRAssembler;

public class Assemble extends Thread {
	
	@Override
	public void run() {
		super.run();
		try {
			runTest();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<Text> reads;
	private int merLength;
	
	public Assemble(ArrayList<Text> reads, int merLength) {
		this.reads= reads;
		this.merLength = merLength;
	}

	public static void main(String[] args) 
			throws IOException, ClassNotFoundException, InterruptedException {
	}
	
	public  void runTest() throws IOException, ClassNotFoundException, InterruptedException{
		Configuration conf = new Configuration();
		cleanupTest(conf);
		setupTest(conf);
		
		MRAssembler assembler = new MRAssembler(merLength, COVERAGE);
		System.out.println("Starting to run test with merLength "+ merLength + " and Coverage " + COVERAGE);
		assembler.setOnFinishListener(new MRAssembler.OnFinishListener() {
			
			@Override
			public void onFinish(ArrayList<String> results) {
				if(onAssembleFinishListener != null){
					onAssembleFinishListener.onAssembleFinish(results);
				}
			}
			
			@Override
			public void onError(String message){
				if(onAssembleFinishListener !=null){
					onAssembleFinishListener.onAssembleError(message);
				}
			}
			
			@Override
			public void onProgress(String prog){
				if(onAssembleFinishListener !=null){
					onAssembleFinishListener.onAssembleProgress(prog);
				}
			}
		});
		assembler.run(new Path(testInput), new Path(testOutput));
		
		cleanupTest(conf);
		
	}
	
	private  void setupTest(Configuration conf) throws IOException {
		FileSystem fileSystem = FileSystem.get(conf);
		
		Path path = new Path(testInput);
		if (fileSystem.exists(path))
			fileSystem.delete(path, true);
		
		//String seq = SequenceHelper.generateSequence(options);
		String seq = "TTCAGCGCGCAGGTTTGGGTCGAGATAAAATCACCAGTACCCAAGACCAGGGGGGCTCGGCGCGTTGGCTAATCCTGGTACATCTTGTTATGAATATTCAGTAGAAAATCTGTGTTAGAGGGACGAGTCACCATGTACCAAAAGCGATATTAATCGGTGGGAGTATTCATCGTGGTGAAGACGCTGGGTTTACGTGGGAAAGGTGCTTGTGTCCCAACAGGCTAGGATATAATGCTGAAACCGTCCCCCAAGCGTTCAGGGTGGGGTTTGCTACAACTTCCGAGTCCAATGTGTCCGTGTTCATGATATATATGCTCAAGGGCGAGAATTGGACCTAGCTTTCGTGTTAGTACGTAGCATGGTGACACAAGCACAGTAGATCCTGCCCGCGTATCCTATA";
		
		//ArrayList<Text> reads = SequenceHelper.split(50 , 100, seq, COVERAGE);
		
                /*
		System.out.println(seq);
		for(Text t : reads){
			System.out.println(t.toString());
		}
		*/
		FSDataOutputStream out = fileSystem.create(path);
		for (Text read : reads) {
			byte[] bytes = read.copyBytes();
			for (byte b : bytes)
				out.write(b);
		}
		out.close();
		
		fileSystem.close();
	}

	private  void cleanupTest(Configuration conf) throws IOException {
		FileSystem fileSystem = FileSystem.get(conf);
		
		fileSystem.delete(new Path(testInput), true);
		fileSystem.delete(new Path(testOutput), true);
		fileSystem.delete(new Path(testTMP), true);
		fileSystem.close();
	}
	
	public interface OnAssembleFinishListener{
		public void onAssembleFinish(ArrayList<String> resultSequences);
		public void onAssembleError(String Message);
		public void onAssembleProgress(String prog);
	}
	
	public void setOnAssembleFinishListener(OnAssembleFinishListener listener){
		onAssembleFinishListener = listener;
	}
	
	public static final int COVERAGE = 6;
	private OnAssembleFinishListener onAssembleFinishListener;

	private static String testInput = new String("MRAssemblerTest_in.txt");
	private static String testOutput = new String("MRAssemblerTest_out");
	private static String testTMP = new String("sabe.MRAssemblerTmp");

}
