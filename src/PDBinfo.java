// Copyright: Baihan Lin, Baker Lab, doerlbh@gmail.com
// Date: Nov 2015

import java.io.*;
import java.util.*;

public class PDBinfo {

	private static final String STNL = "TER";
	private static final String SEQSTART = "ATOM";
	public String name;       
	public ArrayList<String> AA = new ArrayList<String>();
	public int aasize;

	public PDBinfo(File file) throws FileNotFoundException {

		this.name = file.getName();
		AA.add("-"); //turn it into a 1-based list
		aasize = 0;

		@SuppressWarnings("resource")
		Scanner scanin = new Scanner(file);

		if (!scanin.hasNext()) {
			System.out.println("PDB No Next!!!!!");
		}
		if (!scanin.hasNextLine()) {
			System.out.println("PDB No Next Line!!!!!");
		}
		while (scanin.hasNextLine()) {
			//for debugging
			//			System.out.println("PDB  INSIDE!!!!!");

			String next = scanin.nextLine().trim();
			if (next.equals(STNL)) {
				break;
			}
			if (next.startsWith(SEQSTART)) {
				String[] info = next.split("[ \t]+");

				//for debugging
				//			System.out.println(info);

				if (Integer.parseInt(info[5]) > aasize) {
					aasize++;
					System.out.print(aasize + " ");
					AA.add(AA2A(info[3]));
				} 
			}
		}

		//for debugging
		System.out.println(this.name);
		for (int i = 0; i < this.AA.size(); i++) {
			System.out.print(this.AA.get(i));
		}
		System.out.println();
	}

	private String AA2A(String AAlong) {
		String AAshort = ""; 
		switch (AAlong.toLowerCase()) {
		case "ala":  
			AAshort = "A"; 
			break;
		case "arg":  
			AAshort = "R";
			break;
		case "asn":  
			AAshort = "N";
			break;
		case "asp":  
			AAshort = "D";
			break;
		case "cys":  
			AAshort = "C";
			break;
		case "gln":  
			AAshort = "Q";
			break;
		case "glu": 
			AAshort = "E";
			break;
		case "gly":  
			AAshort = "G";
			break;
		case "his":  
			AAshort = "H";
			break;
		case "ile":  
			AAshort = "I";
			break;
		case "leu":  
			AAshort = "L";
			break;
		case "lys":  
			AAshort = "K";
			break;
		case "met":  
			AAshort = "M";
			break;
		case "phe":  
			AAshort = "F";
			break;
		case "pro":  
			AAshort = "P";
			break;
		case "ser":  
			AAshort = "S";
			break;
		case "thr":  
			AAshort = "T";
			break;
		case "trp":  
			AAshort = "W";
			break;
		case "tyr":  
			AAshort = "Y";
			break;
		case "val":  
			AAshort = "V";
			break;
		case "asx":  
			AAshort = "B";
			break;
		case "glx":  
			AAshort = "Z";
			break;
		case "xaa":  
			AAshort = "X";
			break;
		default: 
			AAshort = "*";
			break;
		}
		return AAshort;
	}
}