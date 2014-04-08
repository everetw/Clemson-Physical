/**
 * 
 */
package com.example.clemsonphysical;

/**
 * @author jburton
 *
 */
public class DatabaseSQL {


	public static final String [] CREATE_DATABASE =
		{
///		Formatted with http://www.freeformatter.com/java-dotnet-escape.html
//		" -- Creator:       MySQL Workbench 6.0.9/ExportSQLite plugin 2009.12.02\n"+
//		"-- Author:        James Burton\n"+
//		"-- Caption:       New Model\n"+
//		"-- Project:       Name of the project\n"+
//		"-- Changed:       2014-03-24 16:06\n"+
//		"-- Created:       2014-03-16 14:30\n"+
//		"-- Schema: physical therapy internal\n",

 		
		"PRAGMA foreign_keys = OFF;\n",
		
		"BEGIN;",
	
		"CREATE TABLE \"exercise\"(\n" +
		"  \"idexercise\" INTEGER PRIMARY KEY NOT NULL,\n"+
		"  \"exercise_name\" VARCHAR(45) NOT NULL,\n"+
		"  \"exercise_video_url\" VARCHAR(127),\n"+
		"  \"exercise_instruction_url\" VARCHAR(1023),\n"+
		"  \"exercise_file_location\" VARCHAR(127),\n"+
		"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
		"  \"update_time\" TIMESTAMP,\n"+
		"  CONSTRAINT \"exercise_name_UNIQUE\"\n"+ 
		"    UNIQUE(\"exercise_name\")"+ 
		");",


		"CREATE TABLE \"exercise_annotation\"(\n"+
		"  \"idexercise_annotation\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
		"  \"exercise_idexercise\" INTEGER NOT NULL,\n"+
		"  \"exercise_annotation_video_time\" INTEGER NOT NULL,\n"+
		"  \"exercise_annotation_annotation\" VARCHAR(255) NOT NULL,\n"+
		"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
		"  CONSTRAINT \"fk_exercise_annotation_exercise1\"\n"+
		"    FOREIGN KEY(\"exercise_idexercise\")\n"+
		"    REFERENCES \"exercise\"(\"idexercise\")\n"+
		");\n",
			
		"CREATE INDEX \"exercise_annotation.fk_exercise_annotation_exercise1_idx\" ON \"exercise_annotation\"(\"exercise_idexercise\");\n",
	
		"CREATE TABLE \"exercise_plan\"(\n"+
		"  \"idexercise_plan\" INTEGER PRIMARY KEY NOT NULL,\n"+
		"  \"exercise_plan_name\" VARCHAR(45) NOT NULL,\n"+
		"  \"exercise_plan_description\" VARCHAR(255),\n"+
		"  CONSTRAINT \"exercise_plan_name_UNIQUE\"\n" +
		"  UNIQUE(\"exercise_plan_name\")\n"+
		");\n",

		"CREATE TABLE \"exercise_plan_item\"(\n"+
		"  \"exercise_plan_item_id\" INTEGER PRIMARY KEY NOT NULL,\n"+
		"  \"exercise_plan_idexercise_plan\" INTEGER NOT NULL,\n"+
		"  \"exercise_plan_item_sequence\" INTEGER NOT NULL,\n"+
		"  \"exercise_plan_item_quantity\" INTEGER NOT NULL,\n"+
		"  \"exercise_plan_item_description\" VARCHAR(255),\n"+
		"  \"exercise_idexercise\" INTEGER NOT NULL,\n"+
		"  CONSTRAINT \"fk_exercise_plan_item_exercise_plan1\"\n"+
		"    FOREIGN KEY(\"exercise_plan_idexercise_plan\")\n"+
		"    REFERENCES \"exercise_plan\"(\"idexercise_plan\"),\n"+
		"  CONSTRAINT \"fk_exercise_plan_item_exercise1\"\n"+
		"    FOREIGN KEY(\"exercise_idexercise\")\n"+
		"    REFERENCES \"exercise\"(\"idexercise\")\n"+
		");\n",
	
		"CREATE INDEX \"exercise_plan_item.fk_exercise_plan_item_exercise_plan1_idx\" ON \"exercise_plan_item\"(\"exercise_plan_idexercise_plan\");\n",
		
		"CREATE INDEX \"exercise_plan_item.fk_exercise_plan_item_exercise1_idx\" ON \"exercise_plan_item\"(\"exercise_idexercise\");\n",
		
		"CREATE INDEX \"exercise_plan_item.plan_exercise_sequence\" ON \"exercise_plan_item\"(\"exercise_plan_idexercise_plan\",\"exercise_idexercise\",\"exercise_plan_item_sequence\");",
		
		"CREATE TABLE \"exercise_log\"(\n"+
		"  \"idexercise_log\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
		"  \"exercise_log_video_location\" VARCHAR(255) NOT NULL,\n"+
		"  \"exercise_log_video_notes\" VARCHAR(255),\n"+
		"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
		"  \"exercise_idexercise\" INTEGER NOT NULL,\n"+
		"  CONSTRAINT \"fk_exercise_log_exercise1\"\n"+
		"    FOREIGN KEY(\"exercise_idexercise\")\n"+
		"    REFERENCES \"exercise\"(\"idexercise\"),\n"+
		"  CONSTRAINT \"exercise_log_video_location_UNIQUE\"\n" +
		"  UNIQUE(\"exercise_log_video_location\")\n"+
		");\n",
		
		"CREATE INDEX \"exercise_log.fk_exercise_log_exercise1_idx\" ON \"exercise_log\"(\"exercise_idexercise\");\n",

		"CREATE TABLE \"exercise_log_annotation\"(\n"+
		"  \"idexercise_log_annotation\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
		"  \"exercise_log_idexercise_log\" INTEGER NOT NULL,\n"+
		"  \"exercise_log_annotation_video_time\" INTEGER NOT NULL,\n"+
		"  \"exercise_log_annotation_annotation\" VARCHAR(255) NOT NULL,\n"+
		"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
		"  CONSTRAINT \"fk_exercise_log_annotation_exercise_log1\"\n"+
		"    FOREIGN KEY(\"exercise_log_idexercise_log\")\n"+
		"    REFERENCES \"exercise_log\"(\"idexercise_log\")\n"+
		");\n",
		
		"CREATE INDEX \"exercise_log_annotation.fk_exercise_log_annotation_exercise_log1_idx\" ON \"exercise_log_annotation\"(\"exercise_log_idexercise_log\");\n",

		"COMMIT;\n"
		};
	
	public static final String [] DROP_DATABASE =
		{
		"PRAGMA foreign_keys = OFF;\n",
		"DROP TABLE IF EXISTS \"exercise_log_annotation\"",
		"DROP TABLE IF EXISTS \"exercise_log\"",
		"DROP TABLE IF EXISTS \"exercise_plan_item\"",
		"DROP TABLE IF EXISTS \"exercise_plan\"",
		"DROP TABLE IF EXISTS \"exercise_annotation\"",
		"DROP TABLE IF EXISTS \"exercise\"",
		"COMMIT;\n"
		};


}
