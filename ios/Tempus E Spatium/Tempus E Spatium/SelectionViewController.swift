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

class SelectionViewController: UIViewController, GADBannerViewDelegate {

    @IBOutlet weak var mAdView: GADBannerView!
    
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

        let request = GADRequest()
        
        mAdView.adUnitID = "ca-app-pub-9627209153774793/9924537425" //ca-app-pub-9627209153774793/2302339919"
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
