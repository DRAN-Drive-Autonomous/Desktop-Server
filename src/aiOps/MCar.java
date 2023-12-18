package aiOps;

public class MCar extends PIDController{
	public String name;
	public String owner;
	
	public MCar() {
		super(0.5f, 0.05f, 0.01f);
		this.name = "Obey Tailgater";
		this.owner = "Michael De Santa";
	}
}
