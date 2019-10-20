//
//  LearnViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 15/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import WebKit
import SwiftIcons

class LearnViewController: UIViewController {

    @IBOutlet weak var mHeader: UILabel!
    
    @IBOutlet weak var mWebView: WKWebView!
    
    var mUrl:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mHeader.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.book), iconColor: UIColor(named: "warning")!, postfixText: NSLocalizedString("learn", comment: ""), postfixTextColor: UIColor(named: "warning")!, size: nil, iconSize: nil)
        mWebView.load(URLRequest(url: URL(string: mUrl!)!))
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
