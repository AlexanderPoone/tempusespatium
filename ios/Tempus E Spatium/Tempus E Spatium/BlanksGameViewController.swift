//
//  BlanksGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 14/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import WebKit

extension UIColor {
    var hexString: String {
        let colorRef = cgColor.components
        let r = colorRef?[0] ?? 0
        let g = colorRef?[1] ?? 0
        let b = ((colorRef?.count ?? 0) > 2 ? colorRef?[2] : g) ?? 0
        let a = cgColor.alpha

        var color = String(
            format: "#%02lX%02lX%02lX",
            lroundf(Float(r * 255)),
            lroundf(Float(g * 255)),
            lroundf(Float(b * 255))
        )

        if a < 1 {
            color += String(format: "%02lX", lroundf(Float(a)))
        }

        return color
    }
}

class BlanksGameViewController: UIViewController {

    @IBOutlet weak var mWebView: WKWebView!
    
    @IBOutlet weak var mKeyboardArea: UIView!
    
    private let mPreferences = UserDefaults.standard

    override func viewDidLoad() {
        super.viewDidLoad()

        mWebView.loadHTMLString("<html><head></head><body style=\"background-color:\(UIColor(named: self.mPreferences.string(forKey: "PREF_PLAYER_1_THEME")!)!.hexString);\"><small>Test!</small></body></html>", baseURL: nil)
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
