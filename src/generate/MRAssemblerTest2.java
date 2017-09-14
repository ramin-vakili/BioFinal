// Copyright (c) 2014 Philip M. Hubbard
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
// 
// http://opensource.org/licenses/MIT

package generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException; 
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import com.philiphubbard.sabe.MRAssembler;

// A sample driver application for running the MRAssembler class with Hadoop.
// The test data in this case is a larger set of reads, containing errors and
// repetitive sections repeated once ("single repeats"), and a goal that is 
// a longer sequence.

public class MRAssemblerTest2 {
	
	public static void main(String[] args) 
			throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		cleanupTest(conf);
		setupTest(conf);
		
		MRAssembler assembler = new MRAssembler(MER_LENGTH, COVERAGE);
		assembler.run(new Path(testInput), new Path(testOutput));
		
		verifyTest(conf);
		cleanupTest(conf);
		
		System.exit(0);
	}
	
	private static void setupTest(Configuration conf) throws IOException {
		FileSystem fileSystem = FileSystem.get(conf);
		
		Path path = new Path(testInput);
		if (fileSystem.exists(path))
			fileSystem.delete(path, true);
		
		ArrayList<Text> reads = new ArrayList<Text>();
		
		/*
		reads.add(new Text("TTCAGCGCGCAGGTTTGGGTCGAGATAAAATCACCAGTACCCAAGACCAGGGGGG\n"));
		reads.add(new Text("CTCGGCGCGTTGGCTAATCCTGGTACATCTTGTTATGAATATTCAGTAGAAAATCTGTGTTAGAGGGACGAGTCACCATGTACCAAAAGCGATATTAATCG\n"));
		reads.add(new Text("GTGGGAGTATTCATCGTGGTGAAGACGCTGGGTTTACGTGGGAAAGGTGCTTGTGTCCCAACAGGCTAGGATATAATGCTGAAACCGTC\n"));
		reads.add(new Text("CCCCAAGCGTTCAGGGTGGGGTTTGCTACAACTTCCGAGTCCAATGTGTC\n"));

		reads.add(new Text("CGTGTTCATGATATATATGCTCAAGGGCGAGAATTGGACCTAGCTTTCGTGTTAGTACGTAGCATGGTGACACAAGCACAGTAGATCCTGCCCGCGTATCCTATA\n"));
		reads.add(new Text("TTCAGCGCGCAGGTTTGGGTCGA\n"));
		reads.add(new Text("GATAAAATCACCAGTACCCAAGACCAGGGGGGCTCGGCGCGTTGGCTAATCCTGGTACATCTTGTTATGAATATTCAGTAGAAAA\n"));

		reads.add(new Text("TCTGTGTTAGAGGGACGAGTCACCATGTACCAAAAGCGATATTAATCGGTGGGAGTATTCATC"));
		reads.add(new Text("GTGGTGAAGACGCTGGGTTTACGTGGGAAAGGTGCTTGTGTCCCAACAGGCTAGGATATAATGCTGAAACCGTCCCCCAAGCGTTCAGGGTGGGGTTTGCTACAACTTCCGAGTCCA\n"));
		reads.add(new Text("ATGTGTCCGTGTTCATGATATATATGCTCAAGGGCGAGAATTGGACCTAGCTTTCGTGTTAGTACGTAGCATGGTGACACAAGCACAGTAGATCCTGCCCGCGTATCCTATA\n"));

		reads.add(new Text("TTCAGCGCGCAGGTTTGGGTCGAGATAAAATCACCAGTACCCAAGACCAGGGGGGCTCGGCGCGTTGGCTAATCCTGGTACATCTTGTTATGAATATTCAGTAG\n"));
		reads.add(new Text("AAAATCTGTGTTAGAGGGACGAGTCACCATGTACCAAAAGCGATATTAATCGGTGGGAGTATTCATCGTGGTGAAGACGCTGGGTTTACGTGGG\n"));
		reads.add(new Text("AAAGGTGCTTGTGTCCCAACAGGCTAGGATATAATGCTGAAACCGTCCCCCAAGCGTTCAGGGTGGGGTTTGCTACAACTTCCGAGTCCAATGTGTCCGTGTTCATGATATATATGC\n"));

		reads.add(new Text("TCAAGGGCGAGAATTGGACCTAGCTTTCGTGTTAGTACGTAGCATGGTGACACAAGCACAGTAGATCCTGCCCGCGTATCCTATA"));
		reads.add(new Text("TTCAGCG\n"));
		reads.add(new Text("CGCAGGTTTGGGTCGAGATAAAATCACCAGTACCCAA\n"));
		reads.add(new Text("GACCAGGGGGGCTCGGCGCGTTGGCTAATCCTGGTACATCTTGTTATGAATATTCAGTAGAAAATCTGTGTT\n"));
		reads.add(new Text("AGAGGGACGAGTCACCATGTACCAAAAGCGATATTAATCGGTGGG\n"));
		reads.add(new Text("AGTATTCATCGTGGTGAAGACGCTGGGTTTACGTGGGAAAGGTGCTTGTGTCCCAACAGGCTAGGATATAATGCTGAAACCGTCCCCCAAGCGTTCAGGGTG\n"));
		reads.add(new Text("GGGTTTGCTACAACTTCCGAGTCCAATGTGTCCGTGTTCATGATATATATGCTCAAGGGCGAGAATTGGACCTAGCTTTCGTGTTAGTAC\n"));
		reads.add(new Text("GTAGCATGGTGACACAAGCACAGTAGATCCTGCCCGCGTATCCTATA\n"));
		reads.add(new Text("TTCAGCGCGCAGGTTTGGGTCGAGATAAAATCACCAGTACCCAAGACCAGGGGGGCTCGGCGCGTTGGCTAATCCTGGTACATCTTGTTATGAATATTCAGTAGAAAATCTGTGTTAGAGGGA\n"));
		reads.add(new Text("CGAGTCACCATGTACCAAAAGCGATATTAATCGGTGGGAGTATTCATCGTGGTGAAGACGCTGGGTTTACGTGGGAAAGGTGCTTGTGTCCCAACAGGCTAGG\n"));
		reads.add(new Text("ATATAATGCTGAAACCGTCCCCCAAGCGTTCAGGGTGGGGTTTGCTACAACTTCCGAGTCCAATGTGT\n"));
		reads.add(new Text("CCGTGTTCATGATATATATGCTCAAGGGCGAGAATTGGACCTAGCTTTCGTGTTAGTACGTAGCATGGTGACACAAGCACAGTAGATCCTGCCCGCGTATCCTATA\n"));
		reads.add(new Text("TTCAGCGCGCAGGTTTGGGTCGAGATAAAATCACCAGTACCCAAGAC\n"));
		reads.add(new Text("CAGGGGGGCTCGGCGCGTTGGCTAATCCTGGTACATCTTGTTATGAATATTCAGTAGAAAATCTGTGTTAGAGGGACGAGTCACCATGTACCAAAAGCGAT\n"));
		reads.add(new Text("ATTAATCGGTGGGAGTATTCATCGTGGTGAAGACGCTGGGTTTACGTGGGAAAGGTGCTTGTGTCCCAACAGGCTAGGATATAATGCTGAAACCGTCCCCCAAGCGTTCAGGGTGGG\n"));
		reads.add(new Text("GTTTGCTACAACTTCCGAGTCCAATGTGTCCGTGTTCATGATATATATGCTCAAGGGCGAGAATTGGACCTAGCTTTCGTGTTAGTACGTAGCATGGTGACACAAGCACAGTAGATCCTGCCCGCGTATCCTATA\n"));
		*/
		///*
		try (BufferedReader br = new BufferedReader(new FileReader(new File("reads1.txt")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String s = line+"\n";
		    	reads.add(new Text(s));
		    }
		}
		//*/
		//for(Text t:reads){
			//System.out.println(t.toString());
		//}

		FSDataOutputStream out = fileSystem.create(path);
		for (Text read : reads) {
			byte[] bytes = read.copyBytes();
			for (byte b : bytes)
				out.write(b);
		}
		out.close();
		
		fileSystem.close();
	}

	private static void verifyTest(Configuration conf) throws IOException {
		FileSystem fileSystem = FileSystem.get(conf);
		FSDataInputStream output = fileSystem.open(new Path(testOutput));
		
		System.out.println("Test succeeded.");
	}

	private static void cleanupTest(Configuration conf) throws IOException {
		FileSystem fileSystem = FileSystem.get(conf);
		
		fileSystem.delete(new Path(testTMP), true);
		fileSystem.delete(new Path(testInput), true);
		fileSystem.delete(new Path(testOutput), true);
		
		fileSystem.close();
	}
	
	private static final int MER_LENGTH = 7;
	private static final int COVERAGE = 6;

	private static String testInput = new String("MRAssemblerTest_in.txt");
	private static String testOutput = new String("MRAssemblerTest_out");
	private static String testTMP = new String("sabe.MRAssemblerTmp");
}
