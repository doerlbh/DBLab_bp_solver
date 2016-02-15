import java.io.*;     // for File, FileNotFoundException
import java.util.*;   // for Scanner, List, Set, Collections

public class bp_solver {

	private static final String TEMPBP = "template.bp";
	private static final String SELEFILE = "result_selected_4_5";
	private static final String OFILE = "test11";
	private static final String OUTP = "test11";
	private static final String PATH = "/Users/DoerLBH/Dropbox/Eclipse/workspace/bp_solver/";
//	private static final String TESTPDB = "/Users/DoerLBH/Dropbox/Eclipse/workspace/bp_solver/domain_swap_heterodimer/18A19B/all_models_350/18A19B_renum_0001-ext_ends_0_loop_len_4_Amin2.pdb";
	private static final String STNL1 = "BLOCK1";
	private static final String STNL2 = "BLOCK2";
	//	private static final String STNL3 = "TER";
	private static final String PDB = ".pdb";
//	private static final String ADD = "0 x PIKAA "; //test7
//	private static final String ADD = "0 x . "; //test8
	private static final String ADD = "0 "; //test9
//	private static final String ADD2 = "";
//	private static final String ADD2 = " L"; //test10
	private static final String ADD2 = " ."; //test11
	//	private static final String LEN = "_len_";
	private static final String PATHABBP = "/Users/DoerLBH/Dropbox/Eclipse/"
			+ "workspace/bp_solver/domain_swap_heterodimer/18A19B/18A19B_bp/";
	private static final String PATHBABP = "/Users/DoerLBH/Dropbox/Eclipse/"
			+ "workspace/bp_solver/domain_swap_heterodimer/18B19A/18B19A_bp/";
	private static final String PATHBAPDB = "/Users/DoerLBH/Dropbox/Eclipse/"
			+ "workspace/bp_solver/domain_swap_heterodimer/18B19A/all_models_330/";
	private static final String PATHABPDB = "/Users/DoerLBH/Dropbox/Eclipse/"
			+ "workspace/bp_solver/domain_swap_heterodimer/18A19B/all_models_350/";
	private static List<String> templateList = new ArrayList<String>();
	private static Map<String, BPinfo> bpList = new TreeMap<String, BPinfo>();
	private static Map<String, PDBinfo> pdbList = new TreeMap<String, PDBinfo>();


	public static void main(String[] args) throws IOException {	

		// open source file
		//		System.out.println("What is the name of the file containing general bp file? ");
		//		Scanner console1 = new Scanner(System.in);
		//		String templateFile = console1.nextLine();
		String templateFile = TEMPBP;

		//		System.out.println("What is the file containing list of the desired looping design?");
		//		Scanner console2 = new Scanner(System.in);
		//		String selectedFile = console2.nextLine();
		String selectedFile = SELEFILE;

		//		System.out.println("What is the prefix of the result file?");
		//		Scanner console3 = new Scanner(System.in);
		//		String outFile = console3.nextLine();
		String outFile = OFILE;

		//		System.out.println("What is the path of the data folder?");
		//		Scanner console4 = new Scanner(System.in);
		//		String pathS = console4.nextLine();
		//		File path = new File(pathS);
		File path = new File(PATH);
		String dir = path.getPath();
		File oPath = new File(PATH + OUTP);
		if (!oPath.exists()) {
			if (oPath.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		String oDir = oPath.getPath();

		Map<String, ArrayList<String>> resultList = new TreeMap<String, ArrayList<String>>();
		List<String> List18A19B = new ArrayList<String>();
		List<String> List18B19A = new ArrayList<String>();

		@SuppressWarnings("resource")
		Scanner interestin = new Scanner(new File(dir, selectedFile));
		while (interestin.hasNextLine()) {
			String next = interestin.nextLine().trim();
			if (next.startsWith("18A19B")) {
				List18A19B.add(next);
			} else if (next.startsWith("18B19A")) {
				List18B19A.add(next);
			} 
		}	

		@SuppressWarnings("resource")
		Scanner templateIn = new Scanner(new File(dir, templateFile));
		while (templateIn.hasNextLine()) {
			templateList.add(templateIn.nextLine());
		}

		//		for (String modelAB : List18A19B) {
		//			for (String modelBA : List18B19A) {
		//				resultList.put(outFile + "-" + modelAB.substring(0, modelAB.length()-4)
		//				+ "-" + modelBA.substring(0, modelAB.length()-4) + 
		//				".bp", replacePDB2BP(path, modelAB, modelBA, -1, -1, -1, -1, "", ""));
		//			}
		//		}

		// for debugging
		System.out.println("---List------ Debug ---------");

		System.out.println("--------DEBUG-18A19B-------------");
		System.out.println("--------------------------------");

		infoSetUp(List18A19B, PATHABBP, PATHABPDB);

		System.out.println("--------------------------------");
		System.out.println();
		System.out.println("--------DEBUG-18B19A-------------");
		System.out.println("--------------------------------");

		infoSetUp(List18B19A, PATHBABP, PATHBAPDB);	

		System.out.println("--------------------------------");
		System.out.println();

		System.out.println("---HERE STARTS COMBO--------------------------");
		System.out.println();

		int absCount = 0;
		int countAB = 0;		
		int countBA = 0;
		for (String modelAB : List18A19B) {
			countAB++;
			countBA = 0;
			for (String modelBA : List18B19A) {
				countBA++;
				absCount++;

				System.out.println();
				System.out.println("COMBO : " + countAB + " - " + countBA + " #" + absCount);
				System.out.println(modelAB);
				System.out.println(modelBA);

				resultList.put(outFile + "-C" + String.format("%03d", absCount) + "-" 
						+ countAB + "-" + countBA
						+ "-" + modelAB.substring(0, modelAB.length()-4)
						+ "-" + modelBA.substring(0, modelAB.length()-4) 
						+ ".bp", newReplacement(modelAB, modelBA));

				System.out.println("---------");

			}
		}

		for (String fout : resultList.keySet()) {

			File result = new File(oDir, fout);
			result.createNewFile(); 
			PrintStream output = new PrintStream(result);

			for (int r = 0; r < resultList.get(fout).size(); r++) {
				output.print(resultList.get(fout).get(r));
				output.println();
			}

			output.close();
			System.out.println("Finished! " + fout);
		}

	}

	private static void infoSetUp(List<String> list, String bpPath, String pdbPath) throws FileNotFoundException {
		int icount = 0;
		for (String model : list) {
			icount++;

			String bpFName = bpPath + model.substring(18, model.indexOf(PDB)) + ".bp";
			File bpFile = new File(bpFName);	
			BPinfo bp = new BPinfo(bpFile);
			bpList.put(model, bp);
			int len = bp.end - bp.start;

			String pdbFName = pdbPath + model;
			File pdbFile = new File(pdbFName);
			//			File pdbFile = new File(TESTPDB);

			PDBinfo pdb = new PDBinfo(pdbFile);
			pdbList.put(model, pdb);

			//for debugging
			System.out.println("--------------------");
			System.out.println(icount + " " + model.substring(18, model.indexOf(PDB)));
			System.out.println(bpFName);
			System.out.println(pdbFName);
			//			System.out.println(TESTPDB);
			//			if (pdbFName.equals(TESTPDB)) {
			//				System.out.println("same!!!!!!!!!");
			//			} else {
			//				System.out.println("different!!!!!");
			//			}
			System.out.println("len: " + len);		
			System.out.println("start: " + bp.start);
			System.out.println("end  : " + bp.end);
			System.out.println();
		}
	}

	private static ArrayList<String> newReplacement(String modelAB, String modelBA) throws FileNotFoundException {

		ArrayList<String> outputList = new ArrayList<String>();

		String AB = getAA(modelAB);
		String BA = getAA(modelBA);

		System.out.println("AB  : " + AB);
		System.out.println("BA  : " + BA);

		for (int t = 0; t < templateList.size(); t++) {
			if (templateList.get(t).endsWith(STNL1)) {
				char[] ABchar = AB.toCharArray();
				for (char charE : ABchar) {
					outputList.add(ADD + charE + ADD2);
				}
			} else if (templateList.get(t).endsWith(STNL2)) {
				char[] BAchar = BA.toCharArray();
				for (char charE : BAchar) {
					outputList.add(ADD + charE + ADD2);
				}
			} else {
				outputList.add(templateList.get(t));
			}
		}
		return  outputList;
	}

	private static String getAA(String model) {
		String AA = "";
		int bpS = bpList.get(model).start;
		int bpE = bpList.get(model).end;
		for (int i = bpS; i < bpE; i++) {
			AA = AA + pdbList.get(model).AA.get(i);
		}
		return AA;
	}

}

//	private static int[] getIndex(File bpfile) throws FileNotFoundException {
//		@SuppressWarnings("resource")
//		Scanner scanin = new Scanner(bpfile);
//		int[] index = {0, 0};
//		int indexCur = 0;
//		while (scanin.hasNextLine()) {
//			if (index[0] != 0 && index[1] != 0) {
//				return index;
//			}
//			String next = scanin.nextLine().trim();
//			indexCur++;
//			if (index[0] == 0) {
//				if (next.startsWith("0")) {
//					index[0] = indexCur;
//				}
//			} else {
//				if (!next.startsWith("0")) {
//					index[1] = indexCur;
//				}
//			}
//		}
//		index[0] = -1000;
//		index[1] = -1000;
//		return index;
//	}
//}

//	private static ArrayList<String> doReplacement(String modelAB, String modelBA) throws FileNotFoundException {
//		ArrayList<String> outputList = new ArrayList<String>();
//		
//		String bpABfName = PATHABBP + modelAB.substring(18, modelAB.indexOf(PDB)) + ".bp";
//		String bpBAfName = PATHBABP + modelBA.substring(18, modelBA.indexOf(PDB)) + ".bp";
//		String pdbABfName = PATHABPDB + modelAB;
//		String pdbBAfName = PATHBAPDB + modelBA;
//
//		File bpABFile = new File(bpABfName);
//		File bpBAFile = new File(bpBAfName);
//		File pdbABFile = new File(pdbABfName);
//		File pdbBAFile = new File(pdbBAfName);
//
//		int indexLenAB = modelAB.indexOf(LEN);
//		int indexLenBA = modelBA.indexOf(LEN);
//		int lenAB = Integer.parseInt(modelAB.substring(indexLenAB + 5, indexLenAB + 6));
//		int lenBA = Integer.parseInt(modelBA.substring(indexLenBA + 5, indexLenBA + 6));
//		int indexAB = getIndex(bpABFile);
//		int indexBA = getIndex(bpBAFile);
//		
//		Scanner inAB = new Scanner(pdbABFile);
//		int ABpdbIndex = 0;
//		while (inAB.hasNextLine()) {
//			ABpdbIndex++;
//			if ((ABpdbIndex >= indexAB) && (ABpdbIndex < indexAB + lenAB)) {
//				String next = inAB.nextLine().trim();
//				AB = AB + next.substring(next.length() - 1);
//			} else if (ABpdbIndex >= indexAB + lenAB) {
//				break;
//			}
//		}
//
//		Scanner inBA = new Scanner(pdbBAFile);
//		int BApdbIndex = 0;
//		while (inBA.hasNextLine()) {
//			BApdbIndex++;
//			if ((BApdbIndex >= indexBA) && (BApdbIndex < indexBA + lenBA)) {
//				String next = inBA.nextLine().trim();
//				BA = BA + next.substring(next.length() - 1);
//			} else if (BApdbIndex >= indexBA + lenBA) {
//				break;
//			}
//		}
//
//		//for debugging
//		System.out.println("---AB------- Debug ---------");
//		System.out.println(modelAB);
//		System.out.println("lenAB: " + lenAB);		
//		System.out.println("indexAB: " + indexAB);		
//		System.out.println("---BA------ Debug ---------");
//		System.out.println(modelBA);
//		System.out.println("lenBA: " + lenBA);		
//		System.out.println("indexBA: " + indexBA);		
//		System.out.println("----------- Debug ---------");
//
//		if ((indexAB != -1) && (indexBA != -1) && (!AB.isEmpty()) && (!BA.isEmpty())) {
//			for (int t = 0; t < templateList.size(); t++) {
//				if (templateList.get(t).endsWith(STNL1)) {
//					char[] ABchar = AB.toCharArray();
//					for (char charE : ABchar) {
//						outputList.add("0 x PIKAA " + charE);
//					}
//				} else if (templateList.get(t).endsWith(STNL2)) {
//					char[] BAchar = BA.toCharArray();
//					for (char charE : BAchar) {
//						outputList.add("0 x PIKAA " + charE);
//					}
//				} else {
//					outputList.add(templateList.get(t));
//				}
//			}
//		}
//		return  outputList;
//	}

//	private static ArrayList<String> replacePDB2BP(File file, String modelAB, String modelBA, 
//			int indexAB, int indexBA, int lenAB, int lenBA, String AB, String BA) 
//					throws FileNotFoundException {
//		ArrayList<String> outputList = new ArrayList<String>();
//		if ((indexAB != -1) && (indexBA != -1) && (!AB.isEmpty()) && (!BA.isEmpty())) {
//			for (int t = 0; t < templateList.size(); t++) {
//				if (templateList.get(t).endsWith(STNL1)) {
//					char[] ABchar = AB.toCharArray();
//					for (char charE : ABchar) {
//						outputList.add("0 x PIKAA " + charE);
//					}
//				} else if (templateList.get(t).endsWith(STNL1)) {
//					char[] BAchar = BA.toCharArray();
//					for (char charE : BAchar) {
//						outputList.add("0 x PIKAA " + charE);
//					}
//				} else {
//					outputList.add(templateList.get(t));
//				}
//			}
//			return outputList;
//		}
//
//		String fName = file.getName();
//		if(file.isDirectory()){
//			File dirAllFiles[] = file.listFiles();
//			for (File dirFile : dirAllFiles) {
//				return replacePDB2BP(dirFile, modelAB, modelBA, indexAB, indexBA, lenAB, lenBA, AB, BA);		
//			}
//		}
//		if (indexAB == -1) {
//			if (fName.endsWith(modelAB.substring(18, modelAB.indexOf(PDB)))) {
//				int indexLen1 = modelAB.indexOf(LEN);
//				lenAB = Integer.parseInt(modelAB.substring(indexLen1 + 5, indexLen1 + 6));
//				indexAB = getIndex(file);			
//			}
//			return replacePDB2BP(file, modelAB, modelBA, indexAB, indexBA, lenAB, lenBA, AB, BA);
//		}
//		if (indexBA == -1) {
//			if (fName.endsWith(modelBA.substring(18, modelBA.indexOf(PDB)))) {
//				int indexLen2 = modelBA.indexOf(LEN);
//				lenBA = Integer.parseInt(modelBA.substring(indexLen2 + 5, indexLen2 + 6));
//				indexBA = getIndex(file);
//			} 
//			return replacePDB2BP(file, modelAB, modelBA, indexAB, indexBA, lenAB, lenBA, AB, BA);
//		}
//		if (AB.isEmpty()) {
//			if (fName.endsWith(modelAB)) {
//				Scanner inAB = new Scanner(file);
//				int pdbIndex = 0;
//				while (inAB.hasNextLine()) {
//					pdbIndex++;
//					if ((pdbIndex >= indexAB) && (pdbIndex < indexAB + lenAB)) {
//						String next = inAB.nextLine().trim();
//						AB = AB + next.substring(next.length() - 1);
//					} else if (pdbIndex >= indexAB + lenAB) {
//						break;
//					}
//				}
//			}
//			return replacePDB2BP(file, modelAB, modelBA, indexAB, indexBA, lenAB, lenBA, AB, BA);
//		}
//		if (BA.isEmpty()) {
//			if (fName.endsWith(modelBA)) {
//				Scanner inBA = new Scanner(file);
//				int pdbIndex = 0;
//				while (inBA.hasNextLine()) {
//					pdbIndex++;
//					if ((pdbIndex >= indexBA) && (pdbIndex < indexBA + lenBA)) {
//						String next = inBA.nextLine().trim();
//						BA = BA + next.substring(next.length() - 1);
//					} else if (pdbIndex >= indexBA + lenBA) {
//						break;
//					}
//				}
//			}
//			return  replacePDB2BP(file, modelAB, modelBA, indexAB, indexBA, lenAB, lenBA, AB, BA);
//		}
//		return  outputList;
//	}
