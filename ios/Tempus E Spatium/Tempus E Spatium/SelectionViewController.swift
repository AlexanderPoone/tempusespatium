//
//  SelectionViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import GoogleMobileAds
import SwiftIcons
import AVFoundation
import AudioToolbox
import Reachability
//import NotificationCenter

class SelectionViewController: UIViewController, GADBannerViewDelegate {
    
    var beep, swoosh: AVAudioPlayer?
    var player : AVPlayer?
    
    @IBOutlet weak var mAdView: GADBannerView!
    
    @IBOutlet weak var mPlayBtn: UIButton!
    @IBOutlet weak var mRulesBtn: UIButton!
    @IBOutlet weak var mSettingsBtn: UIButton!
    @IBOutlet weak var mHighscoresBtn: UIButton!
    @IBOutlet weak var mQuitBtn: UIButton!
    
    @IBAction func unwindToSelectionViewController(segue: UIStoryboardSegue) {
    }
    
    @IBAction func mPlayBtnClicked() {
        //Reachability MUST be used!
        swoosh!.play()
        if self.player != nil {
            print("stopped")
            self.player!.pause()
            self.player = nil
            print("player deallocated")
        } else {
            print("player was already deallocated")
        }
        if (Reachability()!.connection == .none) {
            let alert = UIAlertController(title: nil, message: NSLocalizedString("err_no_network", comment: ""), preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: NSLocalizedString("ok", comment: ""), style: .default, handler: { (alert: UIAlertAction!) in }))
            self.present(alert, animated: false, completion: nil)
        } else {
            performSegue(withIdentifier: "goToSelector", sender: nil)
        }
    }
    
    func setupAudioPlayer(withFile file: String, type: String) -> AVAudioPlayer? {
        let path = Bundle.main.path(forResource: file, ofType: type)
        let url = NSURL.fileURL(withPath: path!)
        return try? AVAudioPlayer(contentsOf: url)
    }
    
    @IBAction func mRulesBtnClicked() {
        beep!.play()
    }
    
    @IBAction func mSettingsBtnClicked() {
        beep!.play()
    }
    
    @IBAction func mHighscoresBtnClicked() {
        beep!.play()
    }
    
    @IBAction func mQuitBtnClicked() {
        beep!.play()
        let alert = UIAlertController(title: nil, message: NSLocalizedString("really_quit", comment: ""), preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("yes", comment: ""), style: .default, handler: { (alert: UIAlertAction!) in
            exit(0)
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("no", comment: ""), style: .default, handler: { (alert: UIAlertAction!) in }))
        self.present(alert, animated: false, completion: nil)
    }
    
    
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
    
    func playWikiWiki() {
        if player == nil {
            do {
                guard let url = URL.init(string: "https://ia800905.us.archive.org/10/items/Wikipedia_201411/Wikipedia.MP3") else { return }
                
                let playerItem = AVPlayerItem(url: url)
                
                player = try AVPlayer(playerItem:playerItem)
                player!.volume = 1.0
                NotificationCenter.default.addObserver(forName: NSNotification.Name.AVPlayerItemDidPlayToEndTime, object: nil, queue: nil) { notification in
                    if self.player != nil {
                        self.player!.seek(to: CMTime.zero)
                        self.player!.play()
                    }
                }
                player!.play()
            } catch let error as NSError {
                self.player = nil
                print(error.localizedDescription)
            } catch {
                print("AVAudioPlayer init failed")
            }
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        playWikiWiki()
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        beep = setupAudioPlayer(withFile: "beep_space_button", type: "wav")
        swoosh = setupAudioPlayer(withFile: "space_swoosh", type: "wav")
        
        view.backgroundColor = UIColor(patternImage: UIImage(named: "navajo")!)
        
        mPlayBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.gamepad), iconColor: .white, postfixText: NSLocalizedString("play", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "info")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mRulesBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeBrands(.leanpub), iconColor: .white, postfixText: NSLocalizedString("rules", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
        
        
        mSettingsBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.cog), iconColor: .white, postfixText: NSLocalizedString("settings", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "secondary")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mHighscoresBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.trophy), iconColor: .white, postfixText: NSLocalizedString("highscores", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "warning")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mQuitBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.signOutAlt), iconColor: .white, postfixText: NSLocalizedString("quit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        
        
        let request = GADRequest()
        
        mAdView.adUnitID = "ca-app-pub-9627209153774793/5575136404" //ca-app-pub-9627209153774793/2302339919"
        mAdView.rootViewController = self
        mAdView.delegate = self
        //        GADMobileAds.sharedInstance().requestConfiguration.testDeviceIdentifiers = ["f040a2fc154731455d1ae667f42eb08c"]
        //        request.testDevices = ["f040a2fc154731455d1ae667f42eb08c"]
        mAdView.adSize = GADAdSize(size: CGSize(width: 320, height: 50), flags: 0)
        mAdView.load(request)
    }
    
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    
}
