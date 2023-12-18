package aiOps;

public class TCar extends PIDController {
	public String name;
	public String owner;
	
	public TCar() {
		super(0.5f, 0.05f, 0.01f);
		this.name = "Canis Bodhi";
		this.owner = "Trevor Philips";
	}
}
