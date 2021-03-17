public class NBody {

	public static String image = "./images/starfield.jpg";

	/* 注意用static，因为test中直接引用的NBody而不是specific object*/
	public static double readRadius(String args) {
		In in = new In(args);
		in.readInt();
		double RadiusInFile = in.readDouble();
		return RadiusInFile;
	}

	public static Body[] readBodies(String args) {
		In in = new In(args);
		int sum = in.readInt();
		Body[] target = new Body[sum];
		in.readDouble();
		for(int i = 0; i < sum; i += 1) {
			target[i] = new Body(in.readDouble(),in.readDouble(),
				in.readDouble(),in.readDouble(),in.readDouble(),in.readString());
		}
		return target;
	}


	public static void main(String[] target) {
		//In in = new In(target);
		//double T = Double.parseDouble(in.readString());
		//double dt = Double.parseDouble(in.readString());
		//String filename = in.readString();
		double T = Double.parseDouble(target[0]);
		double dt = Double.parseDouble(target[1]);
		String filename = target[2];
		Body[] bodies = NBody.readBodies(filename);
		double radius = NBody.readRadius(filename);

		StdDraw.enableDoubleBuffering();
		int waitTimeMilliseconds = 10;

		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0, 0, image);
		StdDraw.show();
		//StdDraw.clear();

		int count = 0;
		while(count <= T) {
			double[] xForces = new double[bodies.length];
			double[] yForces = new double[bodies.length];
			for(int i = 0; i < bodies.length; i += 1) {
				xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
				yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
			}
			for(int i = 0; i < bodies.length; i += 1) {
				bodies[i].update(dt, xForces[i], yForces[i]);
			}
			
		StdDraw.clear();
		StdDraw.picture(0, 0, image);
		for(Body s : bodies) {
			s.draw();
		}
		StdDraw.show();
			count += dt;
		}

		StdDraw.pause(waitTimeMilliseconds);

		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < bodies.length; i++) {
		    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		                  bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
		                  bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);   
		}
	}

}