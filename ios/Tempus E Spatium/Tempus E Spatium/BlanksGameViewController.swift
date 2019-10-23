//
//  BlanksGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 14/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import WebKit

class BlanksGameViewController: UIViewController {

    @IBOutlet weak var mWebView: WKWebView!
    
    @IBOutlet weak var mKeyboardArea: UIView!
    
    private let mPreferences = UserDefaults.standard

    override func viewDidLoad() {
        super.viewDidLoad()

        mWebView.loadHTMLString("<html><head></head><body style=\"background-color:\(self.mPreferences.string(forKey: "PREF_PLAYER_1_THEME")!);\"><small>Test!</small></body></html>", baseURL: nil)
        mWebView.scrollView.panGestureRecognizer.isEnabled = false
        mWebView.scrollView.bounces = false

        if let keyboard = mKeyboardArea.subviews.first {
        keyboard.removeFromSuperview()
        let controller = storyboard!.instantiateViewController(withIdentifier: "fr_KeyboardViewController") as! fr_KeyboardViewController
        controller.view.frame = mKeyboardArea.bounds
        controller.willMove(toParent: self)
        mKeyboardArea.addSubview(controller.view)
        addChild(controller)
        
        controller.didMove(toParent: self)
        }
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
