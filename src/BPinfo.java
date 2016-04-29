// Copyright: Baihan Lin, Baker Lab, doerlbh@gmail.com
// Date: Nov 2015

import java.io.*;
import java.util.*;

public class BPinfo {

	public String name;       
	public int start;
	public int end;

	@SuppressWarnings("resource")
	public BPinfo(File file) throws FileNotFoundException {
		

		this.name = file.getName();
		
		this.start = -1;
		this.end = -1;
		
		Scanner scanin = new Scanner(file);
		int indexCur = 0;
		while (scanin.hasNextLine()) {
//			System.out.println("BP INSIDE!!!!!");
			if (this.start != -1 && this.end != -1) {
				break;
			}
			String next = scanin.nextLine().trim();
			indexCur++;
			if (this.start == -1) {
				if (next.startsWith("0")) {
					this.start = indexCur;
				}
			} else {
				if (!next.startsWith("0")) {
					this.end = indexCur;
				}
			}
			
		}
		
	}
	
}
