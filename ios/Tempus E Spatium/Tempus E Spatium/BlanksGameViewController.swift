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
    
    override func viewDidLoad() {
        super.viewDidLoad()

        mWebView.loadHTMLString("<html><head></head><body><small>Test!</small></body></html>", baseURL: nil)
        mWebView.scrollView.panGestureRecognizer.isEnabled = false
        mWebView.scrollView.bounces = false
        // Do any additional setup after loading the view.
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
