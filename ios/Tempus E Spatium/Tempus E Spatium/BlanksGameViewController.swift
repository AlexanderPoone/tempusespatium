//
//  BlanksGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 14/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import WebKit
import SwiftIcons

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
    
    private let mContentController = WKUserContentController()

    
    @IBOutlet weak var mResetBtn: UIButton!
    
    @IBOutlet weak var mSubmitBtn: UIButton!
    
    var mPointChange = 0
    
    @IBAction func mReset(_ sender: Any) {
        mWebView.evaluateJavaScript("document.write('asdf');", completionHandler: nil)
//        let script = "document.write('asdf');"
//        let userScript = WKUserScript(source: script, injectionTime: .atDocumentEnd, forMainFrameOnly: true)
//
//        mContentController.addUserScript(userScript)
//        mWebView.configuration.userContentController = mContentController

    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mResetBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mSubmitBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.paperPlane), iconColor: .white, postfixText: NSLocalizedString("submit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
        
        
//        let script =    """
//                        var script = document.createElement('script');
//                        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.2/MathJax.js?config=default&#038;ver=1.3.8';
//                        script.type = 'text/javascript';
//                        document.getElementsByTagName('head')[0].appendChild(script);
//                        """
//        let userScript = WKUserScript(source: script, injectionTime: .atDocumentStart, forMainFrameOnly: true)
//
//
//        mContentController.addUserScript(userScript)
//
//        mWebView.configuration.userContentController = mContentController
        

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
