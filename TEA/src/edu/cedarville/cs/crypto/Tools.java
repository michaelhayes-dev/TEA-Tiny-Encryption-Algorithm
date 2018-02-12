package edu.cedarville.cs.crypto;

import java.nio.ByteBuffer;

public class Tools {
	
	public static Integer[] convertFromBytesToInts(byte[] bs) {
            
            //unsigned integers are represented by 4 bytes
            Integer output[] = new Integer[bs.length/4];
            
            int j=0;
            for(int i=0; i < bs.length/4; i++){
                //convert 4 bytes into a hex string
                String s = "";
                s = s.concat(String.format("%02x", bs[j]));
                s = s.concat(String.format("%02x", bs[j+1]));
                s = s.concat(String.format("%02x", bs[j+2]));
                s = s.concat(String.format("%02x", bs[j+3]));

                //parse that hex string as unsigned
                output[i] = (Integer.parseUnsignedInt(s, 16));
                j+=4;
            }
            return output;
	}
	
	public static Integer[] convertFromHexStringToInts(String s) {
		
            //unsigned integers are represented by 8 hex characters
            Integer output[] = new Integer[s.length()/8];
            
            int j=0;
            for(int i=0; i < s.length()/8; i++){
                //grab the next 8 hex characters and parse them as unsigned
                output[i] = Integer.parseUnsignedInt(s.substring(j, j+8), 16);
                j+=8;
            }
            return output;
	}
	
	public static byte[] convertFromIntsToBytes(Integer[] ints) {
		
            //4 bytes are needed to represent an unsigned integer
            byte output[] = new byte[ints.length*4];
            
            int j=0;
            for(int i=0; i < ints.length; i++){        
                //set up a collection of 4 bytes for the next integer
                byte temp[] = new byte[4];
                //convert the integer into bytes and store in the array
                temp = ByteBuffer.allocate(4).putInt(ints[i]).array();
                
                output[j] = temp[0];
                output[j+1] = temp[1];
                output[j+2] = temp[2];
                output[j+3] = temp[3];
                j+=4;
            }
            return output;
	}
	
	public static String convertFromIntsToHexString(Integer[] ints) {
            
            String s = "";
            
            for(int i=0; i < ints.length; i++){    
                //get the hex value
                String hex = Integer.toHexString(ints[i]);
                
                //add padded zeros to make sure the block size remains the same
                s = s.concat("00000000".substring(hex.length()) + hex);
            }
            return s;
	}
	
}
