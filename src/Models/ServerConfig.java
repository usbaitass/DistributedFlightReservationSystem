package Models;

/**
 * Server Configuration Model
 * @author SajjadAshrafCan
 *
 */
public class ServerConfig {

	public String serverName;
	public String shortServerName;
	public int rmiPort;
	public int udpPort;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerConfig [serverName=" + serverName + ", shortServerName=" + shortServerName + ", rmiPort="
				+ rmiPort + ", udpPort=" + udpPort + "]";
	}

}
