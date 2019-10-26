//
//  DateGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 22/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class MonthPickerDelegate: NSObject, UIPickerViewDataSource, UIPickerViewDelegate {
    
    var mRevealed:Bool = false

    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return (1...12).count
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        
        let string = String(Array(1...12)[row])
        return NSAttributedString(string: string, attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
}

class DateGameViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return (mLowerBoundYear!...mUpperBoundYear!).count
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        
        let string = String(Array(mLowerBoundYear!...mUpperBoundYear!)[row])
        return NSAttributedString(string: string, attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
    
    @IBOutlet weak var mQuestion: UILabel!
    
    @IBOutlet weak var mHintPic: UIImageView!
    
    @IBOutlet weak var mResetBtn: UIButton!
    
    @IBOutlet weak var mSubmitBtn: UIButton!
    
    @IBOutlet weak var mYearScroller: UIPickerView!
    
    @IBOutlet weak var mMonthScroller: UIPickerView!
    
    var mLowerBoundYear, mUpperBoundYear:Int?
    var mYear, mMonth:Int?
        
    var mEvent:String?
    
    let mMonthDelegate = MonthPickerDelegate() //Must be a member, since dataSource and delegate are weak
    
    @IBAction func mResetBtnClicked(_ sender: Any) {
        //        mYearScroller.
        //        mMonthScroller.
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mResetBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mSubmitBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.paperPlane), iconColor: .white, postfixText: NSLocalizedString("submit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        mYearScroller.dataSource = self
        mYearScroller.delegate = self
        mMonthScroller.dataSource = mMonthDelegate
        mMonthScroller.delegate = mMonthDelegate
        
        let question = NSMutableAttributedString()
        let light = UIFont(name: "BakerSignetLT", size: 17)!
        let bold = UIFont(name: "BakerSignetBT-Roman", size: 20)!
        
        question.append(NSAttributedString(string: NSLocalizedString("question_history_1", comment: ""), attributes: [NSAttributedString.Key.font: light]))
        question.append(NSAttributedString(string: mEvent!, attributes: [NSAttributedString.Key.font: bold]))
        question.append(NSAttributedString(string: NSLocalizedString("question_history_2", comment: ""), attributes: [NSAttributedString.Key.font: light]))
        mQuestion.attributedText = question
    }
    
    func reveal() {
        mYearScroller.backgroundColor = UIColor(named: "Firebrick")!
        mMonthScroller.backgroundColor = UIColor(named: "IndianRed")!
        mYearScroller.isUserInteractionEnabled = false
        mMonthScroller.isUserInteractionEnabled = false
        mYearScroller.selectRow(mYear! - mLowerBoundYear!, inComponent: 0, animated: true)
        mMonthScroller.selectRow(mMonth! - 1, inComponent: 0, animated: true)
    }
    
}
