//
//  PrefsTableViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import DropDown
import FlagKit
import GoogleMobileAds

class PrefsTableViewController: UITableViewController, GADBannerViewDelegate {
    
    var mLocaleDropDown:DropDown?
    var mDifficultyDropDown:DropDown?
    var mPlayer1ThemeDropDown:DropDown?
    var mPlayer2ThemeDropDown:DropDown?
    
    @IBOutlet weak var mLocaleTitle: UILabel!
    @IBOutlet weak var mLocaleSubtitle: UILabel!
    
    @IBOutlet weak var mDifficultyTitle: UILabel!
    @IBOutlet weak var mDifficultySubtitle1: UILabel!
    @IBOutlet weak var mDifficultySubtitle2: UILabel!
    
    @IBOutlet weak var mPlayer1ThemeTitle: UILabel!
    @IBOutlet weak var mPlayer1ThemeSubtitle: UILabel!
    
    @IBOutlet weak var mPlayer2ThemeTitle: UILabel!
    @IBOutlet weak var mPlayer2ThemeSubtitle: UILabel!
    
    @IBOutlet weak var mAdView: GADBannerView!
    @IBOutlet weak var mAdView2: GADBannerView!
    
    private let mPreferences = UserDefaults.standard
    
    /// Tells the delegate an ad request loaded an ad.
    func adViewDidReceiveAd(_ bannerView: GADBannerView) {
        print("adViewDidReceiveAd")
    }
    
    /// Tells the delegate an ad request failed.
    func adView(_ bannerView: GADBannerView,
                didFailToReceiveAdWithError error: GADRequestError) {
        print("adView:didFailToReceiveAdWithError: \(error.localizedDescription)")
    }
    
    /// Tells the delegate that a full-screen view will be presented in response
    /// to the user clicking on an ad.
    func adViewWillPresentScreen(_ bannerView: GADBannerView) {
        print("adViewWillPresentScreen")
    }
    
    /// Tells the delegate that the full-screen view will be dismissed.
    func adViewWillDismissScreen(_ bannerView: GADBannerView) {
        print("adViewWillDismissScreen")
    }
    
    /// Tells the delegate that the full-screen view has been dismissed.
    func adViewDidDismissScreen(_ bannerView: GADBannerView) {
        print("adViewDidDismissScreen")
    }
    
    /// Tells the delegate that a user click will open another app (such as
    /// the App Store), backgrounding the current app.
    func adViewWillLeaveApplication(_ bannerView: GADBannerView) {
        print("adViewWillLeaveApplication")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mLocaleTitle.text = NSLocalizedString("pref_general_locale", comment: "")
        mDifficultyTitle.text = NSLocalizedString("pref_difficulty", comment: "")
        mPlayer1ThemeTitle.text = NSLocalizedString("pref_player_1_theme", comment: "")
        mPlayer2ThemeTitle.text = NSLocalizedString("pref_player_2_theme", comment: "")
        
        let request = GADRequest()
        
        mAdView.adUnitID = "ca-app-pub-9627209153774793/8995151446" //ca-app-pub-9627209153774793/2302339919"
        mAdView.rootViewController = self
        mAdView.delegate = self
        //        GADMobileAds.sharedInstance().requestConfiguration.testDeviceIdentifiers = ["f040a2fc154731455d1ae667f42eb08c"]
        //        request.testDevices = ["f040a2fc154731455d1ae667f42eb08c"]
        mAdView.adSize = GADAdSize(size: CGSize(width: 320, height: 50), flags: 0)
        mAdView.load(request)
        
        let request2 = GADRequest()
        
        mAdView2.adUnitID = "ca-app-pub-9627209153774793/4042562887" //ca-app-pub-9627209153774793/2302339919"
        mAdView2.rootViewController = self
        mAdView2.delegate = self
        //        GADMobileAds.sharedInstance().requestConfiguration.testDeviceIdentifiers = ["f040a2fc154731455d1ae667f42eb08c"]
        //        request.testDevices = ["f040a2fc154731455d1ae667f42eb08c"]
        mAdView2.adSize = GADAdSize(size: CGSize(width: 300, height: 250), flags: 0)
        mAdView2.load(request)
                
        
        let langs:[String:[String]] = ["正體中文": ["Chinese (Authentic)", "TW", "zh", "請重新開啟應用程式。", "確定"], "English (UK)": ["English", "GB", "en", "Please relaunch the app.", "OK"], "català": ["Catalan", "AD", "ca", "Reinicieu l'aplicació per favor.", "D'acord"], "français": ["French","FR", "fr", "Priez de redémarrer l'appli.", "OK"], "Deutsch": ["German", "DE", "de", "Wieder öffnet die Applikation bitte.", "OK"], "español": ["Spanish","ES", "es", "Reinicie la aplicación por favor.", "Aceptar"], "日本語": ["Japanese", "JP", "ja", "アプリを再起動して下さい。", "OK"], "Українська": ["Ukrainian","UA", "uk", "Please relaunch the app.", "OK"]]
        
        if let savedLocale = self.mPreferences.string(forKey: "PREF_LOCALE") {
            for x in langs.keys {
                if langs[x]![2] == savedLocale {
                    self.mLocaleSubtitle.text = x
                }
            }
        }
        
        mLocaleDropDown = DropDown()
        mLocaleDropDown!.dataSource = Array(langs.keys)
        mLocaleDropDown!.anchorView = mLocaleSubtitle
        
        mLocaleDropDown!.bottomOffset = CGPoint(x: 0, y:(mLocaleDropDown!.anchorView!.plainView.bounds.height))
        mLocaleDropDown!.cellNib = UINib(nibName: "LocaleDropDownTableViewCell", bundle: nil)
        
        mLocaleDropDown!.customCellConfiguration = { (index: Index, item: String, cell: DropDownCell) -> Void in
            guard let cell = cell as? LocaleDropDownTableViewCell else { return }
            cell.mSubtitle.text = langs[item]![0]
            let flag = Flag(countryCode: langs[item]![1])!
            cell.mFlag.image = flag.originalImage
        }
        
        mLocaleDropDown!.selectionAction = { [unowned self] (index: Int, item: String) in
            
            self.mLocaleSubtitle.text = item
            
            self.mPreferences.set(langs[item]![2], forKey: "PREF_LOCALE")

            UserDefaults.standard.set([langs[item]![2]], forKey: "AppleLanguages")     //zh-Hant-HK
            UserDefaults.standard.synchronize()
            
            let alert = UIAlertController(title: nil, message: NSLocalizedString(langs[item]![3], comment: ""), preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: langs[item]![4], style: .default, handler: {
                (action : UIAlertAction!) -> Void in
                exit(0)
            }))
            self.present(alert, animated: true, completion: nil)
        }
        
        mLocaleDropDown!.selectedTextColor = UIColor(named: "Amber")!
        mLocaleDropDown!.textColor = .white
        
        let difficulties = [NSLocalizedString("pref_difficulty_hard", comment: ""): NSLocalizedString("pref_difficulty_hard_sub", comment: ""), NSLocalizedString("pref_difficulty_insane", comment: ""): NSLocalizedString("pref_difficulty_insane_sub", comment: ""), NSLocalizedString("pref_difficulty_hysterical", comment: ""): NSLocalizedString("pref_difficulty_hysterical_sub", comment: "")]
        let difficultiesKeys = [NSLocalizedString("pref_difficulty_hard", comment: ""), NSLocalizedString("pref_difficulty_insane", comment: ""), NSLocalizedString("pref_difficulty_hysterical", comment: "")]
        
        if let savedDifficulty = self.mPreferences.object(forKey: "PREF_DIFFICULTY") {
            let keyText = difficultiesKeys[savedDifficulty as! Int]
            self.mDifficultySubtitle1.text = keyText
            self.mDifficultySubtitle2.text = difficulties[keyText]
        }
        
        mDifficultyDropDown = DropDown()
        mDifficultyDropDown!.dataSource = difficultiesKeys
        mDifficultyDropDown!.anchorView = mDifficultySubtitle2
        
        mDifficultyDropDown!.cellNib = UINib(nibName: "LocaleDropDownTableViewCell", bundle: nil)
        
        mDifficultyDropDown!.customCellConfiguration = { (index: Index, item: String, cell: DropDownCell) -> Void in
            guard let cell = cell as? LocaleDropDownTableViewCell else { return }
            cell.mSubtitle.numberOfLines = 0
            cell.mSubtitle.text = difficulties[item]
            
            cell.mFlag.isHidden = true
        }
        
        mDifficultyDropDown!.selectionAction = { [unowned self] (index: Int, item: String) in
            
            self.mDifficultySubtitle1.text = item
            self.mDifficultySubtitle2.text = difficulties[item]
            self.mPreferences.set(index, forKey: "PREF_DIFFICULTY")
            
        }
        
        mDifficultyDropDown!.bottomOffset = CGPoint(x: 0, y:(mDifficultyDropDown!.anchorView!.plainView.bounds.height))
        
        mDifficultyDropDown!.selectedTextColor = UIColor(named: "Amber")!
        mDifficultyDropDown!.textColor = .white
        
        
        let colours = ["AliceBlue", "Amber", "CosmicLatte", "Lavender", "MidnightBlue", "Sienna", "UbuntuOrange"]
        
        if let savedPlayer1Theme = self.mPreferences.string(forKey: "PREF_PLAYER_1_THEME") {
            self.mPlayer1ThemeSubtitle.text = savedPlayer1Theme
        }
        
        mPlayer1ThemeDropDown = DropDown()
        mPlayer1ThemeDropDown!.dataSource = colours
        mPlayer1ThemeDropDown!.anchorView = mPlayer1ThemeSubtitle
        
        mPlayer1ThemeDropDown!.cellNib = UINib(nibName: "LocaleDropDownTableViewCell", bundle: nil)
        
        mPlayer1ThemeDropDown!.customCellConfiguration = { (index: Index, item: String, cell: DropDownCell) -> Void in
            guard let cell = cell as? LocaleDropDownTableViewCell else { return }
            cell.mSubtitle.text = item
            
            cell.mFlag.backgroundColor = UIColor(named: item)!
            self.mPreferences.set(item, forKey: "PREF_PLAYER_1_THEME")
        }
        
        mPlayer1ThemeDropDown!.selectionAction = { [unowned self] (index: Int, item: String) in
            self.mPlayer1ThemeSubtitle.text = item
        }
        
        mPlayer1ThemeDropDown!.bottomOffset = CGPoint(x: 0, y:(mPlayer1ThemeDropDown!.anchorView!.plainView.bounds.height))
        
        mPlayer1ThemeDropDown!.selectedTextColor = UIColor(named: "Amber")!
        mPlayer1ThemeDropDown!.textColor = .white
        
        
        
        if let savedPlayer2Theme = self.mPreferences.string(forKey: "PREF_PLAYER_2_THEME") {
            self.mPlayer2ThemeSubtitle.text = savedPlayer2Theme
        }
        
        mPlayer2ThemeDropDown = DropDown()
        mPlayer2ThemeDropDown!.dataSource = colours
        mPlayer2ThemeDropDown!.anchorView = mPlayer2ThemeSubtitle
        
        mPlayer2ThemeDropDown!.cellNib = UINib(nibName: "LocaleDropDownTableViewCell", bundle: nil)
        
        mPlayer2ThemeDropDown!.customCellConfiguration = { (index: Index, item: String, cell: DropDownCell) -> Void in
            guard let cell = cell as? LocaleDropDownTableViewCell else { return }
            cell.mSubtitle.text = item
            
            cell.mFlag.backgroundColor = UIColor(named: item)!
            self.mPreferences.set(item, forKey: "PREF_PLAYER_2_THEME")
        }
        
        mPlayer2ThemeDropDown!.selectionAction = { [unowned self] (index: Int, item: String) in
            self.mPlayer2ThemeSubtitle.text = item
        }
        
        mPlayer2ThemeDropDown!.bottomOffset = CGPoint(x: 0, y:(mPlayer2ThemeDropDown!.anchorView!.plainView.bounds.height))
        
        mPlayer2ThemeDropDown!.selectedTextColor = UIColor(named: "Amber")!
        mPlayer2ThemeDropDown!.textColor = .white
        
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section {
        case 1:
            return NSLocalizedString("pref_general", comment: "")
        case 2:
            return NSLocalizedString("pref_gameplay", comment: "")
        default:
            return nil
            
        }
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 1 {
            switch indexPath.row {
            case 0:
                mLocaleDropDown!.show()
            default:
                break
            }
        } else if indexPath.section == 2 {
            switch indexPath.row {
            case 0:
                mDifficultyDropDown!.show()
            case 1:
                mPlayer1ThemeDropDown!.show()
            case 2:
                mPlayer2ThemeDropDown!.show()
            default:
                break
            }
        }
    }
    
    // MARK: - Table view data source
    
    //    override func numberOfSections(in tableView: UITableView) -> Int {
    //        // #warning Incomplete implementation, return the number of sections
    //        return 0
    //    }
    //
    //    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    //        // #warning Incomplete implementation, return the number of rows
    //        return 0
    //    }
    
    /*
     override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
     let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath)
     
     // Configure the cell...
     
     return cell
     }
     */
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
    
    /*
     // Override to support editing the table view.
     override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
     if editingStyle == .delete {
     // Delete the row from the data source
     tableView.deleteRows(at: [indexPath], with: .fade)
     } else if editingStyle == .insert {
     // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
     }
     }
     */
    
    /*
     // Override to support rearranging the table view.
     override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {
     
     }
     */
    
    /*
     // Override to support conditional rearranging of the table view.
     override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the item to be re-orderable.
     return true
     }
     */
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    
}
