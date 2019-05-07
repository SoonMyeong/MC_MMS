package kr.ac.kaist.message_relaying;
/* -------------------------------------------------------- */
/** 
File name : SessionCountForFiveSecs.java
	This saves session counts for five seconds.
Author : Jaehee Ha (jaehee.ha@kaist.ac.kr)
Creation Date : 2019-05-07
Version : 0.9.0
**/
/* -------------------------------------------------------- */


// TODO: Jaehee will implement this.
public class SessionCountForFiveSecs {
	private long curTimeInMillis = 0;
	private long sessionCount = 0;
	private long pollingSessionCount = 0;
	
 	public SessionCountForFiveSecs (long curTimeMillis) {
 		this.curTimeInMillis = curTimeMillis;
 		this.sessionCount = 0;
 		this.pollingSessionCount = 0;
 	}
 	
 	public long getCurTimeInMillis () {
 		return curTimeInMillis;
 	}
	
	public long getSessionCount() {
		return this.sessionCount;
	}
	public void incSessionCount() {
		this.sessionCount++;
	}
	public void incPollingSessionCount() {
		this.pollingSessionCount++;
	}
	public long getPollingSessionCount() {
		return this.pollingSessionCount;
	}
}
