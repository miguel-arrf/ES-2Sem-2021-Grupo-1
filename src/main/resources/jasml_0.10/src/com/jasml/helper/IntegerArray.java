package com.jasml.helper;

public class IntegerArray {
	private int[] data;
	private int max;
	private int currentSize ;
	private final static int Increment = 10;
	public IntegerArray(int size){
		data = new int[size];
		max=size;
		currentSize =0;
	}
	public void add(int var){
		if(currentSize>=max){
			max = max+Increment;
			int[] ni = new int[max];
			System.arraycopy(data,0,ni,0,data.length);
			data = ni;
			ni = null;
		}
		data[currentSize++]=var;
	}
	public int[] getAll(){
		int[] ret = new int[currentSize];
		System.arraycopy(data,0,ret,0,currentSize);
		return ret;
	}
	
}
