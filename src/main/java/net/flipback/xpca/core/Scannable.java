package net.flipback.xpca.core;

public interface Scannable {
	public void onStartup();
	public void onScan();
	public void onShutdown();
}
