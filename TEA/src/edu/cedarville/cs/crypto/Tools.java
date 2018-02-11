package edu.cedarville.cs.crypto;

public class Tools {
	
	public static Integer[] convertFromBytesToInts(byte[] bs) {
                
            Integer output[] = new Integer[bs.length];
            
            for(int i=0; i < bs.length; i++){
                output[i] = (int) bs[i];
            }
            
            return output;
	}
	
	public static Integer[] convertFromHexStringToInts(String s) {
		
            //unsigned integers are represented by 8 hex characters
            Integer output[] = new Integer[s.length()/8];
            
            int j=0;
            for(int i=0; i < s.length()/8; i++){
                output[i] = Integer.parseUnsignedInt(s.substring(j, j+8), 16);
                j+=8;
            }
            
            return output;
	}
	
	public static byte[] convertFromIntsToBytes(Integer[] ints) {
		
            byte output[] = new byte[ints.length];
            
            for(int i=0; i < ints.length; i++){
                output[i] = ints[i].byteValue();
            }

            return output;
	}
	
	public static String convertFromIntsToHexString(Integer[] ints) {
            
            String s = "";
            
            for(int i=0; i < ints.length; i++){
                s = s.concat(Integer.toHexString(ints[i]));
            }
            
            return s;
	}
	
}
