//
//  HighscoresDBManager.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 21/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import FMDB

class HighscoresDBManager: NSObject {
    let databaseFileName = "database.sqlite"
    
    var pathToDatabase: String!
    
    var database: FMDatabase!
    
    let TABLE_HIGHSCORES = "Highscores"
    
    static let shared: HighscoresDBManager = HighscoresDBManager()
    
    override init() {
        super.init()
        
        let documentsDirectory = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as NSString) as String
        pathToDatabase = documentsDirectory.appending("/\(databaseFileName)")
    }
    
    func createDatabase() -> Bool {
        var created = false
        
        if !FileManager.default.fileExists(atPath: pathToDatabase) {
            database = FMDatabase(path: pathToDatabase!)
            
            if database != nil {
                // Open the database
                if database.open() {
                    do {
                        try database.executeUpdate("CREATE TABLE \(TABLE_HIGHSCORES) (player VARCHAR, score INT)", values: nil)
                        created = true
                    }
                    catch {
                        print("Could not create table.")
                        print(error.localizedDescription)
                    }
                    database.close()
                }
                else {
                    print("Could not open the database.")
                }
            }
        }
        
        return created
    }
    
    func openDatabase() -> Bool {
        if database == nil {
            if FileManager.default.fileExists(atPath: pathToDatabase) {
                database = FMDatabase(path: pathToDatabase)
            }
        }
        
        if database != nil {
            if database.open() {
                return true
            }
        }
        
        return false
    }
    
    func clearHighscores() -> Bool {
        var deleted = false
        
        if openDatabase() {
            let query = "DELETE FROM \(TABLE_HIGHSCORES)"
            
            do {
                try database.executeUpdate(query, values: nil)
                deleted = true
            }
            catch {
                print(error.localizedDescription)
            }
            
            database.close()
        }
        
        return deleted
    }
    
    func insertHighscore(player: String, score: Int) {
        if openDatabase() {
            let query = "INSERT OR IGNORE INTO \(TABLE_HIGHSCORES) ('\(player)', \(score));"

            if !database.executeStatements(query) {
                print("Failed to insert attendance into the database.")
                print(database.lastError(), database.lastErrorMessage())
            }
            
            database.close()
        }
    }
    
    func getHighscores() -> [[Any]] {
        var highscores: [[Any]] = []
        
        if openDatabase() {
            let query = "SELECT * FROM \(TABLE_HIGHSCORES) ORDER BY score DESC"
            
            do {
                let results = try database.executeQuery(query, values: nil)
                
                while results.next() {
                    let highscore = [results.string(forColumn: "player")!, results.int(forColumn: "score")] as [Any]
                    
                    highscores.append(highscore)
                }
            }
            catch {
                print(error.localizedDescription)
            }
            
            database.close()
        }
        
        return highscores
    }
}
