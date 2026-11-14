package com.kissene3.game;

public class Timer {
	long thirties;
	
	public Timer() {
		thirties = 0;
	}
	
	public Timer(long thirties) {
		this.thirties = thirties;
	}
	
	public void increment() {
		this.thirties++;
	}
	
	@SuppressWarnings("unused")
	public void decrement() {
		this.thirties--;
	}
	
	public void setThirties(long thirties) {
		this.thirties = thirties;
	}
	
	public long getThirties() {
		return this.thirties;
	}
	
	public int getTotalSeconds() {
		return Math.toIntExact(this.thirties / 30);
	}
	
	public int getSeconds() {
		return this.getTotalSeconds() % 60;
	}
	
	public int getTotalMinutes() {
		return this.getTotalSeconds() / 60;
	}
	
	public int getMinutes() {
		return this.getTotalMinutes() % 60;
	}
	
	public int getHours() {
		return this.getTotalSeconds() / 3600;
	}
	
	@Override
	public String toString() {
		if (getHours() == 0) {
			return String.format("%02d:%02d", this.getMinutes(), this.getSeconds());
		} else {
			return String.format("%02d:%02d:%02d", this.getHours(), this.getMinutes(), this.getSeconds());
		}
	}
}
