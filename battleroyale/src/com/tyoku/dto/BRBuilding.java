package com.tyoku.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BRBuilding implements Serializable {
	private static final long serialVersionUID = 199035831519635924L;

	private String name;
	private BRBuildBlock home;
	private List<BRBuildBlock> buildblocks;

	public BRBuilding(Player player, String name, Location loc1, Location loc2, Location standLoc) {
		if(player == null || name == null || loc1 == null || loc2 == null || standLoc == null){
			return;
		}
		this.setName(name);
		//座標認識
		int homeX = standLoc.getBlockX();
		int homeY = standLoc.getBlockY();
		int homeZ = standLoc.getBlockY();
		int x1 = loc1.getBlockX();
		int y1 = loc1.getBlockY();
		int z1 = loc1.getBlockZ();
		int x2 = loc2.getBlockX();
		int y2 = loc2.getBlockY();
		int z2 = loc2.getBlockZ();

		System.out.println(String.format("H座標 X:%d Y:%d Z:%d", homeX, homeY, homeZ));
		System.out.println(String.format("1座標 X:%d Y:%d Z:%d", x1, y1, z1));
		System.out.println(String.format("2座標 X:%d Y:%d Z:%d", x2, y2, z2));

		int w = 0;
		if (x1 < x2) {
			w = x1;
			x1 = x2;
			x2 = w;
		}
		if (z1 < z2) {
			w = z1;
			z1 = z2;
			z2 = w;
		}
		if (y1 < y2) {
			w = y1;
			y1 = y2;
			y2 = w;
		}

		//ホームロケーション設定
		home = new BRBuildBlock();
		home.setX(homeX - x1);
		home.setY(homeY - y1);
		home.setZ(homeZ - z1);

		System.out.println(String.format("1座標 X:%d Y:%d Z:%d", x1, y1, z1));
		System.out.println(String.format("2座標 X:%d Y:%d Z:%d", x2, y2, z2));
		//ブロック取得
		buildblocks = new ArrayList<BRBuildBlock>();
		for(int i = x2; i <= x1; i++){
			for(int j = y2; j <= y1; j++){
				for(int k = z2; k <= z1; k++){

					System.out.println("--X"+i +" Y"+ j + " Z" + k);
					BRBuildBlock brblock = new BRBuildBlock();
					brblock.setX(x1 + i - homeX - homeX );
					brblock.setY(y1 + j - homeY - homeY );
					brblock.setZ(z1 + k - homeZ - homeZ );
					Block bl = player.getWorld().getBlockAt(i, j, k);

					System.out.println("bl.getType()->"+bl.getType());
					brblock.setBlockData(bl.getData());
					buildblocks.add(brblock);
				}
			}
		}
	}

	/**
	 * 指定のロケーションにBR建造物を建てる。
	 * @param world
	 * @param buildLocation
	 */
	public void create(Player player){
	}

	/**
	 * このクラスオブジェクトシリアライズしてファイルとして保存する。
	 */
	public boolean save() {

		System.out.println("----save4-1");
		try {
			FileOutputStream outFile = new FileOutputStream(this.name + ".dat");

			System.out.println("----save4-2");
			ObjectOutputStream outObject = new ObjectOutputStream(outFile);

			System.out.println("----save4-3");
			outObject.writeObject(this);
			System.out.println("----save4-4");
			outObject.close();
			outFile.close();
			System.out.println("----save4-5");
		} catch (IOException e) {

			System.out.println("----save4-6");
			e.printStackTrace();
			return false;
		}
		System.out.println("----save4-7");
		return true;
	}

	public boolean isCreatable(){
		return buildblocks != null && buildblocks.size() > 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getBlockNum(){
		return buildblocks.size();
	}

	public BRBuildBlock getHome() {
		return home;
	}

	static public BRBuilding getBuilding(String name) {
		File file = new File(name + ".dat");
		if (file.exists()) {
			FileInputStream inFile = null;
			ObjectInputStream inObject = null;
			try {
				inFile = new FileInputStream(file);
				inObject = new ObjectInputStream(inFile);

				Object obj = inObject.readObject();
				if (obj instanceof BRBuilding) {
					return (BRBuilding) obj;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			} finally {
				if(inObject != null){
					try {
						inObject.close();
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}

				if(inFile != null){
					try {
						inFile.close();
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		} else {
			return null;
		}
		return null;
	}
}
