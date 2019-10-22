//
//  DateGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 22/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class DateGameViewController: UIViewController {
    
    @IBOutlet weak var mQuestion: UILabel!
    
    @IBOutlet weak var mHintPic: UIImageView!
    
    @IBOutlet weak var mResetBtn: UIButton!
    
    @IBOutlet weak var mSubmitBtn: UIButton!
    
    @IBOutlet weak var mYearScroller: UIPickerView!
    
    @IBOutlet weak var mMonthScroller: UIPickerView!
    
    var mYear, mMonth:Int?
    
    @IBAction func mResetBtnClicked(_ sender: Any) {
        //        mYearScroller.
        //        mMonthScroller.
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mResetBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mSubmitBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.paperPlane), iconColor: .white, postfixText: NSLocalizedString("submit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
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
