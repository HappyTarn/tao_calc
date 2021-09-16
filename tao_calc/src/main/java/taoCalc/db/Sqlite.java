package taoCalc.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import taoCalc.Const;
import taoCalc.dto.Member;
import taoCalc.dto.MonsterRate;
import taoCalc.dto.PetInfo;
import taoCalc.dto.PrizeMoneyInfo;
import taoCalc.dto.Rate;
import taoCalc.dto.Summary;

public class Sqlite {

	public static int countSqliteMasterByTableName(String guildId, String tableName) {
		Path p = Paths.get("db/" + guildId + ".db");
		if (!Files.exists(p)) {
			System.out.println("ファイルまたはディレクトリは存在しません");
			Sqlite.init(guildId);
		}

		int result = 0;

		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(
					"SELECT COUNT(*) as total FROM sqlite_master WHERE TYPE='table' AND name='" + tableName + "';");
			while (rs.next()) {
				result = rs.getInt("total");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static void updateMember(String guildId, Member member) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute(
					"update member set exp='" + member.getExp() + "'" +
							",normal=" + member.get通常() + "," +
							"weak_enemy=" + member.get弱敵() + "," +
							"strong_enemy=" + member.get強敵() + "," +
							"super_strong_enemy=" + member.get超強敵() + "," +
							"series=" + member.getシリーズ() + "," +
							"rare=" + member.getレア() + "," +
							"super_rare=" + member.get激レア() + "," +
							"super_rare2=" + member.get超激レア() + "," +
							"saba_limit=" + member.get鯖限() + "," +
							"tohru=" + member.getTohru() +
							",update_date=datetime('now', '+9 hours')" +
							" where id='" + member.getId() + "'");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertMember(String guildId, Member member) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute(
					"insert into member(id,normal,weak_enemy,strong_enemy,super_strong_enemy,series,rare,super_rare,super_rare2,saba_limit,tohru,exp,update_date) values('"
							+ member.getId() + "',"
							+ member.get通常() + ","
							+ member.get弱敵() + ","
							+ member.get強敵() + ","
							+ member.get超強敵() + ","
							+ member.getシリーズ() + ","
							+ member.getレア() + ","
							+ member.get激レア() + ","
							+ member.get超激レア() + ","
							+ member.get鯖限() + ","
							+ member.getTohru() + ",'"
							+ member.getExp() + "',datetime('now', '+9 hours'))");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertSummary(Summary summary) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/common.db");
			statement = connection.createStatement();
			statement.execute(
					"insert into summary("
							+ "guild_id,"
							+ "member_id,"
							+ "combat_count,"
							+ "ground_count,"
							+ "exp,"
							+ "sozai_count,"
							+ "weapon_count,"
							+ "bukikon_count,"
							+ "create_date,"
							+ "ban_count"
							+ ") values('"
							+ summary.getGuildId() + "',"
							+ summary.getMemberId() + ","
							+ summary.getCombatCount() + ","
							+ summary.getGroundCount() + ","
							+ summary.getExp() + ","
							+ summary.getSozaiCount() + ","
							+ summary.getWeaponCount() + ","
							+ summary.getBukikonCount() + ","
							+ "datetime('now', '+9 hours'),"
							+ summary.getBanCount() + ")");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<Summary> selectSummaryOrderByExample(String order,String where) {
		Connection connection = null;
		Statement statement = null;
		List<Summary> result = new ArrayList<Summary>();
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/common.db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select "
					+ "member_id,"
					+ "sum(combat_count) as combat_count,"
					+ "sum(ground_count) as ground_count,"
					+ "sum(exp) as exp,"
					+ "sum(sozai_count) as sozai_count,"
					+ "sum(weapon_count) as weapon_count,"
					+ "sum(bukikon_count) as bukikon_count,"
					+ "sum(ban_count) as ban_count"
					+ " from summary "+where+" group by member_id order by " + order + " desc");
			while (rs.next()) {
				Summary summary = new Summary();
				summary.setMemberId(rs.getString("member_id"));
				summary.setCombatCount(rs.getDouble("combat_count"));
				summary.setGroundCount(rs.getDouble("ground_count"));
				summary.setExp(rs.getDouble("exp"));
				summary.setSozaiCount(rs.getDouble("sozai_count"));
				summary.setWeaponCount(rs.getDouble("weapon_count"));
				summary.setBukikonCount(rs.getDouble("bukikon_count"));
				summary.setBanCount(rs.getDouble("ban_count"));
				result.add(summary);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Member> selectMemberOrderByExpDesc(String guildId) {
		Connection connection = null;
		Statement statement = null;
		List<Member> result = new ArrayList<Member>();
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery("select * from member where exp <> '0' order by cast(exp as Integer) desc");
			while (rs.next()) {
				result.add(new Member(rs.getString("id"), Long.parseLong(rs.getString("exp")),
						rs.getString("update_date")));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Member> selectMemberOrderBySubjugation(String guildId) {
		Connection connection = null;
		Statement statement = null;
		List<Member> result = new ArrayList<Member>();
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(
					"select id,(normal+weak_enemy+strong_enemy+super_strong_enemy+series+rare+super_rare+super_rare2+saba_limit+tohru) as sum "
							+ "from member order by cast((normal+weak_enemy+strong_enemy+super_strong_enemy+series+rare+super_rare+super_rare2+saba_limit+tohru) as Integer) desc");
			while (rs.next()) {
				Member m = new Member(rs.getString("id"));
				m.set合計(Long.parseLong(rs.getString("sum")));
				result.add(m);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Member selectMemberById(String guildId, String id) {
		Connection connection = null;
		Statement statement = null;
		Member result = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from member where id='" + id + "'");
			while (rs.next()) {
				Member member = new Member(rs.getString("id"), Long.parseLong(rs.getString("exp")));
				member.set通常(rs.getLong(Const.normal));
				member.set弱敵(rs.getLong(Const.weak_enemy));
				member.set強敵(rs.getLong(Const.strong_enemy));
				member.set超強敵(rs.getLong(Const.super_strong_enemy));
				member.setシリーズ(rs.getLong(Const.series));
				member.setレア(rs.getLong(Const.rare));
				member.set激レア(rs.getLong(Const.super_rare));
				member.set超激レア(rs.getLong(Const.super_rare2));
				member.set鯖限(rs.getLong(Const.saba_limit));
				member.setTohru(rs.getLong(Const.tohruc));
				result = member;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Rate selectRateByRank(String guildId, String rank) {
		Connection connection = null;
		Statement statement = null;
		Rate result = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from rate where rank='" + rank + "'");
			while (rs.next()) {
				result = new Rate(rs.getString("rank"), Long.parseLong(rs.getString("amount")));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static MonsterRate selectMonsterRateInfoByName(String guildId, String name) {
		Connection connection = null;
		Statement statement = null;
		MonsterRate result = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from monster_rate_info where name='" + name + "'");
			while (rs.next()) {
				result = new MonsterRate(rs.getString("name"), Long.parseLong(rs.getString("amount")));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Rate> selectRate(String guildId, String sql) {
		Connection connection = null;
		Statement statement = null;
		List<Rate> result = new ArrayList<Rate>();
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Rate rate = new Rate(rs.getString("rank"), rs.getLong("amount"));
				result.add(rate);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<MonsterRate> selectMonsterRate(String guildId, String sql) {
		Connection connection = null;
		Statement statement = null;
		List<MonsterRate> result = new ArrayList<MonsterRate>();
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				MonsterRate rate = new MonsterRate(rs.getString("name"), rs.getLong("amount"));
				result.add(rate);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String getRole(String guildId, String column) {
		Connection connection = null;
		Statement statement = null;
		String result = "1";
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from role");
			while (rs.next()) {
				result = rs.getString(column);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void deleteAndInsert(String guildId, String delSql, String insSql) {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute(delSql);
			statement.execute(insSql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void init(String guildId) {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			String sql = "create table rate(rank TEXT,amount INTEGER)";
			statement.execute(sql);
			sql = "create table member(id,exp,normal INTEGER,weak_enemy INTEGER,strong_enemy INTEGER,super_strong_enemy INTEGER,series INTEGER,rare INTEGER,super_rare INTEGER,super_rare2 INTEGER,saba_limit INTEGER,tohru INTEGER,update_date)";
			statement.execute(sql);
			sql = "create table role(role1,role2,role3,role4,role5)";
			statement.execute(sql);
			sql = "insert into role(role1,role2,role3,role4,role5) values('','','','','')";
			statement.execute(sql);

			sql = "create table server_info(exp_info_channel TEXT, prize_info_channel TEXT)";
			statement.execute(sql);
			sql = "insert into server_info(exp_info_channel,prize_info_channel) values('','')";
			statement.execute(sql);

			sql = "create table pet_info(id,n_count INTEGER,r_count INTEGER,sr_count INTEGER,u_count INTEGER,mmo_count INTEGER,tao_count INTEGER)";
			statement.execute(sql);

			sql = "create table monster_rate_info(name,amount)";
			statement.execute(sql);

			sql = "create table prize_money_info(name,exp)";
			statement.execute(sql);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void test() {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/hoge.db");
			statement = connection.createStatement();
			String sql = "select * from fruits";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<ArrayList<String>> getResultexecuteSql(String guildId, String sql) {
		Connection connection = null;
		Statement statement = null;

		//データ格納
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int rscnt = rsmd.getColumnCount();

			while (rs.next()) {
				//1件分のデータ(連想配列)
				ArrayList<String> hdata = new ArrayList<String>();
				if (list.size() == 0) {
					for (int i = 1; i <= rscnt; i++) {
						//フィールド名
						String field = rsmd.getColumnName(i);
						hdata.add(field);
					}
					//1件分のデータを格納
					list.add(hdata);
					hdata = new ArrayList<String>();
				}

				for (int i = 1; i <= rscnt; i++) {
					//フィールド名
					String field = rsmd.getColumnName(i);
					//フィールド名に対するデータ
					String getdata = rs.getString(field);
					if (getdata == null) {
						getdata = "";
					}
					//データ格納(フィールド名, データ)
					hdata.add(getdata);
				}
				//1件分のデータを格納
				list.add(hdata);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static ArrayList<HashMap<String, String>> getMapExecuteSql(String guildId, String sql) {
		Connection connection = null;
		Statement statement = null;

		//データ格納
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int rscnt = rsmd.getColumnCount();
			while (rs.next()) {
				//1件分のデータ(連想配列)
				HashMap<String, String> hdata = new HashMap<String, String>();
				for (int i = 1; i <= rscnt; i++) {
					//フィールド名
					String field = rsmd.getColumnName(i);
					//フィールド名に対するデータ
					String getdata = rs.getString(field);
					if (getdata == null) {
						getdata = "";
					}
					//データ格納(フィールド名, データ)
					hdata.put(field, getdata);
				}
				//1件分のデータを格納
				result.add(hdata);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static ArrayList<ArrayList<String>> executeSql(String guildId, String sql) {
		Connection connection = null;
		Statement statement = null;

		//データ格納
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static void executeSqlNotResult(String guildId, String sql) {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static PetInfo selectPetInfo(String guildId, String id) {

		Connection connection = null;
		Statement statement = null;

		//データ格納
		PetInfo result = new PetInfo();

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from pet_info where id='" + id + "'");
			ResultSetMetaData rsmd = rs.getMetaData();
			int rscnt = rsmd.getColumnCount();
			while (rs.next()) {
				PetInfo petInfo = new PetInfo();
				petInfo.setId(rs.getString("id"));
				petInfo.setnCount(rs.getLong("n_count"));
				petInfo.setrCount(rs.getLong("r_count"));
				petInfo.setSrCount(rs.getLong("sr_count"));
				petInfo.setUnknownCount(rs.getLong("u_count"));
				petInfo.setMmoCount(rs.getLong("mmo_count"));
				petInfo.setTaoCount(rs.getLong("tao_count"));
				result = petInfo;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	public static void insertPetInfo(String guildId, PetInfo petInfo) {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute("insert into pet_info(id,n_count,r_count,sr_count,u_count,mmo_count,tao_count) "
					+ "values('" + petInfo.getId() + "',"
					+ petInfo.getnCount() + ","
					+ petInfo.getrCount() + ","
					+ petInfo.getSrCount() + ","
					+ petInfo.getUnknownCount() + ","
					+ petInfo.getMmoCount() + ","
					+ petInfo.getTaoCount() + ")");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void updatePetInfo(String guildId, PetInfo petInfo) {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute("update pet_info set "
					+ "n_count=" + petInfo.getnCount()
					+ ",r_count=" + petInfo.getrCount()
					+ ",sr_count=" + petInfo.getSrCount()
					+ ",u_count=" + petInfo.getUnknownCount()
					+ ",mmo_count=" + petInfo.getMmoCount()
					+ ",tao_count=" + petInfo.getTaoCount()
					+ " where"
					+ " id='" + petInfo.getId() + "'");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static PrizeMoneyInfo selectPrizeMoneyInfoByName(String guildId, String name) {
		Connection connection = null;
		Statement statement = null;
		PrizeMoneyInfo result = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from prize_money_info where name='" + name + "'");
			while (rs.next()) {
				PrizeMoneyInfo pmi = new PrizeMoneyInfo(rs.getString("name"), Long.parseLong(rs.getString("exp")));
				result = pmi;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void insertPrizeMoneyInfo(String guildId, PrizeMoneyInfo pmi) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute(
					"insert into prize_money_info(name,exp) values('"
							+ pmi.getName() + "','"
							+ pmi.getExp().toString() + "')");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void updatePrizeMoneyInfo(String guildId, PrizeMoneyInfo pmi) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			statement.execute(
					"update prize_money_info set exp='" + pmi.getExp() + "'" +
							" where name='" + pmi.getName() + "'");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<PrizeMoneyInfo> selectPrizeMoneyInfo(String guildId, String sql) {
		Connection connection = null;
		Statement statement = null;
		List<PrizeMoneyInfo> result = new ArrayList<PrizeMoneyInfo>();
		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:db/" + guildId + ".db");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				PrizeMoneyInfo pmi = new PrizeMoneyInfo(rs.getString("name"), rs.getLong("exp"));
				result.add(pmi);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}