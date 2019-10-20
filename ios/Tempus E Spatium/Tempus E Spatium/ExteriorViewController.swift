//
//  ExteriorViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 18/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons
import DACircularProgress

class ExteriorViewController: UIViewController {

    @IBOutlet weak var mPauseBtn: UIButton!
    
    
    @IBOutlet weak var mDonutTime: DACircularProgressView!
    
    
    @IBAction func mPauseBtnClicked(_ sender: Any) {
        performSegue(withIdentifier: "showPauseDialog", sender: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mPauseBtn.setIcon(icon: .fontAwesomeSolid(.pause), iconSize: nil, color: .white, backgroundColor: UIColor(named: "info")!, forState: .normal)
        mDonutTime.setProgress(0.5, animated: true)
        mDonutTime.roundedCorners = 5
        mDonutTime.trackTintColor = UIColor(named: "info")!
        mDonutTime.progressTintColor = UIColor(named: "warning")!
        mDonutTime.largeContentTitle = "0"
    }

}
